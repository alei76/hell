package ps.hell.util.file.monitor;

import java.util.HashSet;

import name.pachler.nio.file.Path;
import name.pachler.nio.file.WatchEvent;

/**
 * 文件状态容器
 * @author Administrator
 *
 */
public class FileStatusBean {
	/**
	 * 文件状态
	 */
	public String status="";
	/**
	 * 文件路径
	 */
	public String fileName=null;
	/**
	 * 是否被过滤
	 */
	public boolean isFilter=false;
	
	public FileStatusBean(WatchEvent event,HashSet<String> filterContain,HashSet<String> filterStartsWith,HashSet<String> filterEndsWith){
		WatchEvent.Kind kind = event.kind();
		WatchEvent e = event;
		Path context = (Path) e.context();
		this.fileName = context.toString().replaceAll("\\\\","/");
		this.status=kind.name();
		for(String filter:filterContain){
			if(this.fileName.contains(filter)){
				this.isFilter=true;
				return;
			}
		}
		for(String filter:filterStartsWith){
			if(this.fileName.startsWith(filter)){
				this.isFilter=true;
				return;
			}
		}
		for(String filter:filterEndsWith){
			if(this.fileName.endsWith(filter)){
				this.isFilter=true;
				return;
			}
		}
	}
	/**
	 * 是否为创建
	 * @return
	 */
	public boolean isCreate(){
		if(status.equals(FileStatusStatic.ENTRY_CREATE)){
			return true;
		}return false;
	}
	/**
	 * 是否为删除
	 * @return
	 */
	public boolean isDelete(){
		if(status.equals(FileStatusStatic.ENTRY_DELETE)){
			return true;
		}return false;
	}
	/**
	 * 是否为修改
	 * @return
	 */
	public boolean isModify(){
		if(status.equals(FileStatusStatic.ENTRY_MODIFY)){
			return true;
		}return false;
	}
	/**
	 * 是否为覆盖
	 * @return
	 */
	public boolean isCover(){
		if(status.equals(FileStatusStatic.OVERFLOW)){
			return true;
		}return false;
	}
	@Override
	public String toString(){
		StringBuffer sb=new StringBuffer();
		if(isCreate()){
			sb.append("创建文件,");
		}else if(isDelete()){
			sb.append("删除文件,");
		}else if(isModify()){
			sb.append("修改文件,");
		}else if(isCover()){
			sb.append("覆盖文件,");
		}else{
			sb.append(this.status+",");
		}
		if(isFilter){
			sb.append("被过滤,");
		}else{
			sb.append("待处理,");
		}
		sb.append(fileName);
		return sb.toString();
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public boolean isFilter() {
		return isFilter;
	}
	public void setFilter(boolean isFilter) {
		this.isFilter = isFilter;
	}
	
}
