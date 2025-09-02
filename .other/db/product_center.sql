CREATE DATABASE product_center DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
use product_center;

-- 产品信息表，包含平台所有产品配置
drop table if exists product_info;
create table product_info
(
    id                      bigint          not null auto_increment comment '主键id',
    product_code            varchar(125)    not null                comment '产品code',
    sub_product_code        varchar(125)    not null                comment '子产品code',
    billing_item_code       varchar(125)    not null                comment '计费项code',
    sub_billing_item_code   varchar(125)    not null                comment '子计费项code',
    product_name            varchar(125)    not null                comment '产品名称',
    sub_product_name        varchar(125)    not null                comment '子产品名称',
    billing_item_name       varchar(125)    not null                comment '计费项名称',
    sub_billing_item_name   varchar(125)    not null                comment '子计费项名称',
    unit                    varchar(15)     default ''              comment '单位，个、次、GB等',
    price                   decimal(10,2)   default '0.00'          comment '用户邮箱, 用于登陆',
    charge_size             decimal(10,2)   default '1.00'          comment '手机号码, 用于登陆',
    status                  tinyint         default '0'             comment '生效状态（1生效 0实效）',
    created_by              varchar(64)     default ''              comment '创建者',
    dt_created              datetime        default current_timestamp comment '创建时间',
    modified_by             varchar(64)     default ''              comment '更新者',
    dt_modified             datetime                                comment '更新时间',
    remark                  varchar(500) default null               comment '备注',
    deleted                 tinyint      default '0'                comment '逻辑删除(0:未删除 1:已删除)',
    primary key (id),
    index product_idx (product_code, sub_product_code),
    index billing_idx (billing_item_code, sub_billing_item_code)
) engine = innodb
  auto_increment = 1 comment = '产品信息表';

-- 各服务的产品计费方式表
# drop table if exists charging;
# create table product_info
# (
#     id                      bigint          not null auto_increment comment '主键id',
#     product_code            varchar(125)    not null comment '产品code',
#     sub_product_code        varchar(125)    not null comment '子产品code',
#     billing_item_code       varchar(125)    not null comment '计费项code',
#     sub_billing_item_code   varchar(125)    not null comment '子计费项code',
#     product_name            varchar(125)    not null comment '产品名称',
#     sub_product_name        varchar(125)    not null comment '子产品名称',
#     billing_item_name       varchar(125)    not null comment '计费项名称',
#     sub_billing_item_name   varchar(125)    not null comment '子计费项名称',
#     unit                    varchar(15)  default '00' comment '用户类型, 00系统用户, 10核心账号, 20子账号',
#     charge                  varchar(50)  default '' comment '用户邮箱, 用于登陆',
#     charge_size             varchar(11)  default '' comment '手机号码, 用于登陆',
#     status                  tinyint      default '0' comment '生效状态（1生效 0实效）',
#     created_by              varchar(64)  default '' comment '创建者',
#     dt_created  datetime     default current_timestamp comment '创建时间',
#     modified_by varchar(64)  default '' comment '更新者',
#     dt_modified datetime comment '更新时间',
#     remark      varchar(500) default null comment '备注',
#     deleted     tinyint      default '0' comment '逻辑删除(0:未删除 1:已删除)',
#     primary key (id),
#     index product_idx (product_code, sub_product_code),
#     index billing_idx (billing_item_code, sub_billing_item_code)
# ) engine = innodb
#   auto_increment = 1 comment = '产品信息表';

