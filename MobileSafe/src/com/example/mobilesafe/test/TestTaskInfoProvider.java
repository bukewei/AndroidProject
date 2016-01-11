package com.example.mobilesafe.test;

import java.util.List;

import com.example.mobilesafe.domain.TaskInfo;
import com.example.mobilesafe.engine.TaskInfoProvider;

import android.test.AndroidTestCase;

public class TestTaskInfoProvider extends AndroidTestCase {

	public void testGetTaskInfos()throws Exception{
		List<TaskInfo> infos=TaskInfoProvider.getTaskInfos(getContext());
		for(TaskInfo info : infos){
			System.out.println(info.toString());
		}
	}
	
}
