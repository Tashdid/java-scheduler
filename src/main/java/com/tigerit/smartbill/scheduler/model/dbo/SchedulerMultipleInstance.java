//package com.tigerit.smartbill.scheduler.model.dbo;
//
//import com.tigerit.smartbill.common.model.dbo.localdb.DbMetaData;
//import lombok.Data;
//
//import javax.persistence.*;
//import java.util.Date;
//
//import static com.tigerit.smartbill.common.model.dbo.localdb.DbMetaData.OracleDataType.*;
//
//@Entity(
//        name = DbMetaData.TABLE_SCHEDULER_MULTIPLE_INSTANCE.TABLE_NAME_IN_JPA
//)
//@Table(
//        name = DbMetaData.TABLE_SCHEDULER_MULTIPLE_INSTANCE.TABLE_NAME_IN_DB
//)
//@Data
//public class SchedulerMultipleInstance {
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
//            columnDefinition = VARCHAR2_16_CHAR,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_MULTIPLE_INSTANCE
//                    .COLUMN_IPADDRESS
//    )
//    private String ipAddress;
//
//    @Column(
//            columnDefinition = NUMBER_1,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_MULTIPLE_INSTANCE
//                    .COLUMN_STATUS
//    )
//    private Integer status;
//
//
//    @Column(
//            columnDefinition = VARCHAR2_50_CHAR,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_MULTIPLE_INSTANCE
//                    .COLUMN_SERVICE_NAME
//    )
//    private String serviceName;
//
//    @Column(
//            columnDefinition = NUMBER_1,
//            name = DbMetaData
//                    .TABLE_SCHEDULER_MULTIPLE_INSTANCE
//                    .COLUMN_SERVICE_STATUS
//    )
//    private Integer serviceStatus;
//
//    @Column(
//            nullable = false,
//            columnDefinition = TIMESTAMP_6,
//            name = DbMetaData.TABLE_SCHEDULER_MULTIPLE_INSTANCE.COLUMN_LAST_START_TIME
//    )
//    private Date lastStartTime;
//
//
//}
//
