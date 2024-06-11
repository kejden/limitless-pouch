package com.kejden.limitlesspouch.service;


import com.kejden.limitlesspouch.model.File;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);

    private final GridFsTemplate gridFsTemplate;
    private final GridFsOperations gridFsOperations;

    public FileService(GridFsTemplate gridFsTemplate, GridFsOperations gridFsOperations) {
        this.gridFsTemplate = gridFsTemplate;
        this.gridFsOperations = gridFsOperations;
    }

    public String uploadFile(MultipartFile file) throws IOException {
        log.debug("Uploading file {}", file.getOriginalFilename());
        DBObject metadata = new BasicDBObject();
        metadata.put("fileSize", file.getSize());
        Object fileId = gridFsOperations.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
        log.debug("File id {}", fileId);
        return fileId.toString();
    }

    public List<String> listAllFiles() {
        List<String> ids = new ArrayList<>();
        for (GridFSFile gridFSFile : gridFsTemplate.find(new Query())) {
            ids.add(gridFSFile.getId().toString());
        }
        log.debug("Found {} files", ids.size());
        log.debug("ids: {}", ids);
        return ids;
    }

    public File downloadFile(String id) throws IOException{
        log.debug("Attempting to download file of ID: {}", id);
        GridFSFile gridFSFile = gridFsTemplate.findOne( new Query(Criteria.where("_id").is(id)));
        File file = new File();
        if(gridFSFile != null && gridFSFile.getMetadata() != null) {
            try{
                file.setFilename(gridFSFile.getFilename());
                file.setFiletype(gridFSFile.getMetadata().get("_contentType").toString());
                file.setFilesize(gridFSFile.getMetadata().get("fileSize").toString());
                log.debug("[File metadata] Filename: {}, Filetype: {}, Filesize: {}", file.getFilename(), file.getFiletype(), file.getFilesize());
            }catch (Exception e){
                log.error(e.getMessage());
                e.printStackTrace();
            }
            try(InputStream inputStream = gridFsOperations.getResource(gridFSFile).getInputStream();){
                file.setFile(inputStream.readAllBytes());
                log.debug("File downloaded");
            }
        }else{
            log.debug("File with ID: {} not found", id);
            file.setFile(null);
        }
        return file;
    }
}
