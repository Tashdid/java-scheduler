//package com.tigerit.smartbill.scheduler.model.dbo;
//
//
//import com.tigerit.smartbill.common.model.dbo.localdb.DbMetaData;
//import lombok.Data;
//
//import javax.persistence.*;
//
//import static com.tigerit.smartbill.common.model.dbo.localdb.DbMetaData.OracleDataType.*;
//
//@Entity(
//        name = DbMetaData.TABLE_SCHEDULER_LIST.TABLE_NAME_IN_JPA
//)
//@Table(
//        name = DbMetaData.TABLE_SCHEDULER_LIST.TABLE_NAME_IN_DB
//)
//@Data
//public class SchedulerList {
//
//    @Id
//    @GeneratedValue(
//            strategy = GenerationType.AUTO
//    )
//    @Column(
//            unique = true,
////            nullable = false,
//            columnDefinition = NUMBER_P19_S0
//    )
//    private Long id;
//
//    @Column(
//            columnDefinition = VARCHAR2_50_CHAR,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_LIST
//                    .COLUMN_NAME
//    )
//    private String name;
//
//    @Column(
//            columnDefinition = NUMBER_1,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_LIST
//                    .COLUMN_STATUS
//    )
//    private Integer status;
//
//    @Column(
//            columnDefinition = VARCHAR2_50_CHAR,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_LIST
//                    .COLUMN_IPADDRESS
//    )
//    private String ipAddress;
//
//    @Column(
//            columnDefinition = NUMBER_1,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_LIST
//                    .COLUMN_MULTIPLE_INSTANCE
//    )
//    private Integer multiple_instance;
//}
