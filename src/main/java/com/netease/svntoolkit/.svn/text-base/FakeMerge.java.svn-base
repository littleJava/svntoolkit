package com.netease.svntoolkit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.tmatesoft.svn.core.SVNLogEntryPath;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import com.netease.svntoolkit.po.Branch;
import com.netease.svntoolkit.po.RepositoryInfo;
import com.netease.svntoolkit.po.User;


public class FakeMerge {
//    private static final Log logger = LogFactory.getLog(FakeMerge.class);
    
    private static Set<String> updated = new LinkedHashSet<String>();
    private static Set<String> deleted = new LinkedHashSet<String>(); 
    
    /**
     * merge the changes from branch to trunk.
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        //init the xml resource handler
        Serializer serializer = new Persister();
        RepositoryInfo repositoryInfo = serializer.read(RepositoryInfo.class, Thread.currentThread().getContextClassLoader().getResourceAsStream("svn-fake-merge.xml"));
        
        // svnkit init
        DAVRepositoryFactory.setup( );
        SVNRepository repository = getRepository(repositoryInfo.getBranch().getUrl(),repositoryInfo.getUser());
        
        //access the changelog from svn
        mergeChanges(repository,repositoryInfo.getBranch(),repositoryInfo.getWc().getPath());
        System.out.println("修改或新添加的文件：");
        for (String file : updated) {
            System.out.println(file);
        }
        System.out.println("删除的文件：");
        for (String file : deleted) {
            System.out.println(file);
        }
    }
    
    public static String getCtx(String branchUrl) throws SVNException{
        return SVNURL.parseURIEncoded(branchUrl).getPath();
    }
    
    public static SVNRepository getRepository(String branchUrl,User user) throws SVNException{
        SVNRepository repository = SVNRepositoryFactory.create(SVNURL.parseURIEncoded(branchUrl));
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager( user.getName(),user.getPassword());
        repository.setAuthenticationManager( authManager );
        return repository;
    }
    /**
     * get the changelogs and copy to the tmp dir
     * @param repository
     * @param branch
     * @param workingCopy
     * @throws SVNException
     * @throws IOException
     */
    public static void mergeChanges(SVNRepository repository,Branch branch,String workingCopy) throws SVNException, IOException {
        Collection<SVNLogEntry> logEntries = repository.log(new String[] { "" }, null, branch.getStartRevision(), branch.getEndRevision(), true, true);
        for (Iterator entries = logEntries.iterator(); entries.hasNext();) {
            SVNLogEntry logEntry = (SVNLogEntry) entries.next();
            handleSvnFile(repository, branch, workingCopy, logEntry);
        }
    }
    
    private static void handleSvnFile(SVNRepository repository, Branch branch, String workingCopy, SVNLogEntry logEntry) {
        try {
            System.out.println("---------------------------------------------");
            System.out.println("revision: " + logEntry.getRevision());
            System.out.println("author: " + logEntry.getAuthor());
            System.out.println("date: " + logEntry.getDate());
            System.out.println("log message: " + logEntry.getMessage());
            if (!branch.isTargetUser(logEntry.getAuthor())) {
                return;
            }
            if (!new LogMessageFilter().filter(logEntry.getMessage())) {
                return;
            }
            if (logEntry.getChangedPaths().size() > 0) {
                System.out.println();
                System.out.println("changed paths:");
                Set changedPathsSet = logEntry.getChangedPaths().keySet();
                for (Iterator changedPaths = changedPathsSet.iterator(); changedPaths.hasNext();) {
                    SVNLogEntryPath svnEntryPath = (SVNLogEntryPath) logEntry.getChangedPaths().get(changedPaths.next());
                    System.out.println( " "
                            + svnEntryPath.getType( )
                            + " "
                            + (svnEntryPath.getKind())
                            + " " 
                            + svnEntryPath.getPath( )
                            + ( ( svnEntryPath.getCopyPath( ) != null ) ? " (from "
                                    + svnEntryPath.getCopyPath( ) + " revision "
                                    + svnEntryPath.getCopyRevision( ) + ")" : "" ) );
                    
                    File localFile = new File(workingCopy + svnEntryPath.getPath().substring(getCtx(branch.getUrl()).length()));//generate file in the temporary dir
                    if (svnEntryPath.getType() == SVNLogEntryPath.TYPE_DELETED) {
                        localFile.delete();
                        deleted.add(svnEntryPath.getPath());
                        continue;
                    }
                    if (!localFile.exists()) {//make dir for the local file
                            new File(localFile.getParent()).mkdirs();
                    }
                    if (localFile.isDirectory()) {
                        continue;
                    }
                    downloadSvnFile(repository, svnEntryPath,localFile);
                }
            }
        } catch (Exception e) {
            System.out.println("FakeMerge.handleSvnFile() error!");
        }
    }
    
    private static void downloadSvnFile(SVNRepository repository,SVNLogEntryPath svnEntryPath,File localFile){
        SVNProperties fileProperties = new SVNProperties();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileOutputStream fos = null;
        try {
            repository.getFile(svnEntryPath.getPath(), -1l, fileProperties, baos);
            byte[] html = new byte[6]; 
            byte[] contents = baos.toByteArray();
            for (int i = 0; i < contents.length; i++) {
                if (i == 6) {
                    break;
                }
                html[i] = contents[i];
            }
            if("<html>".equals(new String(html))){//the svn file is directory
                localFile.delete();
            }
            else {
                fos = new FileOutputStream(localFile);
                baos.writeTo(fos);
            }
            updated.add(svnEntryPath.getPath());
        } catch (Exception e) {
            System.out.println("FakeMege.downloadSvnFile() error!");
            if(e.getMessage().contains("path not found: 404 Not Found")){
                deleted.add(svnEntryPath.getPath());
            }
            e.printStackTrace();
        }finally{
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos!=null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
