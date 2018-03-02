package jc.cici.android.atom.bean;

/**
 * 获取笔记，答疑开放权限
 * Created by atom on 2017/7/18.
 */

public class NoteOrQuesStatus {

    // 答疑权限
    private int StageProblem;
    // 笔记权限
    private int StageNote;
    // 资料权限
    private int StageInformation;

    public int getStageProblem() {
        return StageProblem;
    }

    public void setStageProblem(int stageProblem) {
        StageProblem = stageProblem;
    }

    public int getStageNote() {
        return StageNote;
    }

    public void setStageNote(int stageNote) {
        StageNote = stageNote;
    }

    public int getStageInformation() {
        return StageInformation;
    }

    public void setStageInformation(int stageInformation) {
        StageInformation = stageInformation;
    }
}
