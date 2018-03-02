package cn.jun.bean;


import java.util.ArrayList;

public class RaiseQuestionBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int TemplateId;
        private String LecturerName;
        private String LecturerHead;
        private int ListCount;
        private ArrayList<QuestionList> QuestionList;

        public class QuestionList {
            private int QuestionPKID;
            private String QuestionContent;
            //            private ArrayList<String> Answers;
            private ArrayList<Answers> Answers;
            private boolean isChoose = false;

            public ArrayList<RaiseQuestionBean.Body.QuestionList.Answers> getAnswers() {
                return Answers;
            }

            public void setAnswers(ArrayList<RaiseQuestionBean.Body.QuestionList.Answers> answers) {
                Answers = answers;
            }

            public class Answers {
                private int AnswerID;//答案ID
                private String AnswerContent;//答案内容
                private boolean AnswerIsChoose = false;

                public boolean isAnswerIsChoose() {
                    return AnswerIsChoose;
                }

                public void setAnswerIsChoose(boolean answerIsChoose) {
                    AnswerIsChoose = answerIsChoose;
                }

                public int getAnswerID() {
                    return AnswerID;
                }

                public void setAnswerID(int answerID) {
                    AnswerID = answerID;
                }

                public String getAnswerContent() {
                    return AnswerContent;
                }

                public void setAnswerContent(String answerContent) {
                    AnswerContent = answerContent;
                }
            }

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }


            //            private String raise; //评价
//
//            public String getRaise() {
//                return raise;
//            }
//
//            public void setRaise(String raise) {
//                this.raise = raise;
//            }

            public int getQuestionPKID() {
                return QuestionPKID;
            }

            public void setQuestionPKID(int questionPKID) {
                QuestionPKID = questionPKID;
            }

            public String getQuestionContent() {
                return QuestionContent;
            }

            public void setQuestionContent(String questionContent) {
                QuestionContent = questionContent;
            }
        }

        public int getTemplateId() {
            return TemplateId;
        }

        public void setTemplateId(int templateId) {
            TemplateId = templateId;
        }

        public String getLecturerName() {
            return LecturerName;
        }

        public void setLecturerName(String lecturerName) {
            LecturerName = lecturerName;
        }

        public String getLecturerHead() {
            return LecturerHead;
        }

        public void setLecturerHead(String lecturerHead) {
            LecturerHead = lecturerHead;
        }

        public int getListCount() {
            return ListCount;
        }

        public void setListCount(int listCount) {
            ListCount = listCount;
        }

        public ArrayList<RaiseQuestionBean.Body.QuestionList> getQuestionList() {
            return QuestionList;
        }

        public void setQuestionList(ArrayList<RaiseQuestionBean.Body.QuestionList> questionList) {
            QuestionList = questionList;
        }
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public RaiseQuestionBean.Body getBody() {
        return Body;
    }

    public void setBody(RaiseQuestionBean.Body body) {
        Body = body;
    }
}
