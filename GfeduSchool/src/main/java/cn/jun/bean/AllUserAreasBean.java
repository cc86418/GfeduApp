package cn.jun.bean;

import java.util.ArrayList;

public class AllUserAreasBean {
	// 状态码
	private String ResultState;
	// 状态参数
	private String ResultStr;

	private ArrayList<ResultData> ResultData;

	public class ResultData {
		// 项目ID
		private String ProID;
		// 项目名称
		private String ProName;
		// 项目服务器选择状态
		private String ProStatus;
		// 项目本地选择状态
		private String ProLocalStatus;

		public String getProID() {
			return ProID;
		}

		public void setProID(String proID) {
			ProID = proID;
		}

		public String getProName() {
			return ProName;
		}

		public void setProName(String proName) {
			ProName = proName;
		}

		public String getProStatus() {
			return ProStatus;
		}

		public void setProStatus(String proStatus) {
			ProStatus = proStatus;
		}

		public String getProLocalStatus() {
			return ProLocalStatus;
		}

		public void setProLocalStatus(String proLocalStatus) {
			ProLocalStatus = proLocalStatus;
		}

	}

	public String getResultState() {
		return ResultState;
	}

	public void setResultState(String resultState) {
		ResultState = resultState;
	}

	public String getResultStr() {
		return ResultStr;
	}

	public void setResultStr(String resultStr) {
		ResultStr = resultStr;
	}

	public ArrayList<ResultData> getResultData() {
		return ResultData;
	}

	public void setResultData(ArrayList<ResultData> resultData) {
		ResultData = resultData;
	}

}
