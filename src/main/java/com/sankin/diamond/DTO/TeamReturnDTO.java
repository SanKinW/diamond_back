package com.sankin.diamond.DTO;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class TeamReturnDTO {
    private Integer id;
    private String teamName;
    private String basicInformation;
    private Integer creator;
    private String creatorName;
    private List<SmallUserDTO> membersName;
    private Timestamp createTime;
    private List<SmallDocDTO> docs;
}
