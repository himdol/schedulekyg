package com.schedule.schedulekyg.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class CSVUtil {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * csv파일 생성
     *
     * @param csvPath   csv파일 경로
     * @param title     csv 타이틀
     * @param data      데이터
     * @return void
     */
    public void makeCsvFile(String csvPath, String title, List<String> data) {
        BufferedWriter bw = null;
        try{
            logger.info("========== csv파일 생성 시작 ==========");
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8));
            bw.write(title);
            bw.newLine();
            for(String row :data){
                bw.write(row);
                bw.newLine();
            }
            logger.info("========== csv파일 생성 종료 ==========");
        }catch (Exception e){
            throw new RuntimeException("csv 파일 생성 실패 => "+ e.getMessage());
        } finally {
            try{
                if(bw != null){
                    bw.flush();
                    bw.close();
                }
            } catch (IOException e) {
                logger.error("로그인 csv파일 생성 오류 발생 =>{}", e.getMessage());

            }
        }
    }
}
