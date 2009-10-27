package eads;

public class Worker {
	private int skillNumber;
	private int workStartTime;
	private int workEndTime;
	private int earliestBreakTime;
	private int latestBreakTime;
	private int breakDuration;
	/**
	 * @param skillNumber the skillNumber to set
	 */
	public void setSkillNumber(int skillNumber) {
		this.skillNumber = skillNumber;
	}
	/**
	 * @return the skillNumber
	 */
	public int getSkillNumber() {
		return skillNumber;
	}
	/**
	 * @param workStartTime the workStartTime to set
	 */
	public void setWorkStartTime(int workStartTime) {
		this.workStartTime = workStartTime;
	}
	/**
	 * @return the workStartTime
	 */
	public int getWorkStartTime() {
		return workStartTime;
	}
	/**
	 * @param workEndTime the workEndTime to set
	 */
	public void setWorkEndTime(int workEndTime) {
		this.workEndTime = workEndTime;
	}
	/**
	 * @return the workEndTime
	 */
	public int getWorkEndTime() {
		return workEndTime;
	}
	/**
	 * @param earliestBreakTime the earliestBreakTime to set
	 */
	public void setEarliestBreakTime(int earliestBreakTime) {
		this.earliestBreakTime = earliestBreakTime;
	}
	/**
	 * @return the earliestBreakTime
	 */
	public int getEarliestBreakTime() {
		return earliestBreakTime;
	}
	/**
	 * @param latestBreakTime the latestBreakTime to set
	 */
	public void setLatestBreakTime(int latestBreakTime) {
		this.latestBreakTime = latestBreakTime;
	}
	/**
	 * @return the latestBreakTime
	 */
	public int getLatestBreakTime() {
		return latestBreakTime;
	}
	/**
	 * @param breakDuration the breakDuration to set
	 */
	public void setBreakDuration(int breakDuration) {
		this.breakDuration = breakDuration;
	}
	/**
	 * @return the breakDuration
	 */
	public int getBreakDuration() {
		return breakDuration;
	}
	/**
	 * @param skillNumber
	 * @param workStartTime
	 * @param workEndTime
	 * @param earliestBreakTime
	 * @param latestBreakTime
	 * @param breakDuration
	 */
	public Worker(int skillNumber, int workStartTime, int workEndTime,
			int earliestBreakTime, int latestBreakTime, int breakDuration) {
		this.skillNumber = skillNumber;
		this.workStartTime = workStartTime;
		this.workEndTime = workEndTime;
		this.earliestBreakTime = earliestBreakTime;
		this.latestBreakTime = latestBreakTime;
		this.breakDuration = breakDuration;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Worker [skillNumber=" + skillNumber + ", workStartTime="
				+ workStartTime + ", workEndTime=" + workEndTime
				+ ", earliestBreakTime=" + earliestBreakTime
				+ ", latestBreakTime=" + latestBreakTime + ", breakDuration="
				+ breakDuration + "]";
	}
	
	
}
