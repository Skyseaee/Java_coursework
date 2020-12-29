package FileClass;
import java.io.FileNotFoundException;
import java.util.Date;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.io.File;

public class Commit{
    private String hashCode; //当前被commit的文件夹的哈希值
    private String author;
    private String committer;
    private String comment;
    private String timeStamp;
    private String lastKey;
    private String treeKey;
    private final String type = "Commit";

    /**
     * 构造一个Commit
     * @param head 保存当前Head信息的文件
     * @param treeKey 当前被commit的文件夹的哈希值
     * @param author commit的作者
     * @param committer commit的提交者
     * @param comment commit的备注
     * @param newPath 新生成的文件所在的地址
     * @throws Exception
     */
    public Commit(File head,String treeKey,String author,String committer,String comment,String newPath) throws Exception {
        if(!head.exists()) {
            this.author = author;
            this.committer = committer;
            this.comment = comment;
            this.timeStamp = (new Date()).toString();
            this.treeKey = treeKey;
            this.lastKey = null;
            setHashCode();
            String newPath01 = newPath + "\\" + hashCode + ".txt";
            GitUtils.generateFolderValue(new File(newPath01), this.toString());
            String newPath02 = newPath + "\\" + "head.txt";
            GitUtils.generateFolderValue(new File(newPath02), hashCode);
        }
        else {
            this.lastKey = getLastKey(head);
            if(treeKey.equals(lastKey)) {
                this.author = author;
                this.committer = committer;
                this.comment = comment;
                this.timeStamp = (new Date()).toString();
                this.treeKey = treeKey;
                setHashCode();
                newPath = newPath + '\\' + hashCode + ".txt";
                GitUtils.generateFolderValue(new File(newPath), this.toString());
                GitUtils.writeLine(head, hashCode);
            }
        }
    }

    public String getLastKey(File head) {
        try {
            String lastCommitKey = GitUtils.readFirstLine(head);
            File lastCommitFile = GitUtils.findFile(lastCommitKey, head.getParent());
//            return lastCommitKey;
            try{
                assert lastCommitFile != null;
                String res = (GitUtils.readFirstLine(lastCommitFile).split(" "))[1];
                return res;
            }
            catch (FileNotFoundException e) {
                System.out.println("file not found");
                return null;
            }
        } 
        catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException exist");
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException exist");
            return null;
        }
    }

    public String getHashCode() {
        return hashCode;
    }

    public void setHashCode() {
        try {
            this.hashCode = GitUtils.HashCompute(new ByteArrayInputStream(toString().getBytes()));
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "Tree " + treeKey + '\n' + "parent " + lastKey + '\n' + "author " + author + '\n' +"committer " + committer + '\n' + comment + '\n' + timeStamp;
    }

}