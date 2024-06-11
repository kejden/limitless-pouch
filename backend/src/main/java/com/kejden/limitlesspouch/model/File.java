package com.kejden.limitlesspouch.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class File {
    private String filename;
    private String filetype;
    private String filesize;
    private byte[] file;
}
