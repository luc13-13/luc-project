# CREATE DATABASE luc_system DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
use luc_system;

-- 用户表，包含管理员、核心账号、子账号
drop table if exists sys_user;
create table sys_user (
    id                bigint          not null auto_increment    comment '主键id',
    user_id           varchar(31)     not null                   comment '用户ID',
    dept_id           varchar(31)     default null               comment '部门ID',
    user_name         varchar(30)     not null                   comment '用户账号, 用于登陆',
    nick_name         varchar(30)     not null                   comment '用户昵称',
    user_type         varchar(15)     default '00'               comment '用户类型, 00系统用户, 10核心账号, 20子账号',
    email             varchar(50)     default ''                 comment '用户邮箱, 用于登陆',
    phone             varchar(11)     default ''                 comment '手机号码, 用于登陆',
    sex               tinyint         default '2'                comment '用户性别（0男 1女 2未知）',
    avatar            varchar(100)    default ''                 comment '头像地址',
    password          varchar(512)    default ''                 comment '密码，bcrypt加密后的数据',
    status            tinyint         default '1'                comment '帐号状态（0禁用 1正常）',
    login_ip          varchar(128)    default ''                 comment '最后登录IP',
    login_date        datetime                                   comment '最后登录时间',
    created_by        varchar(64)     default ''                 comment '创建者',
    dt_created        datetime        default current_timestamp  comment '创建时间',
    modified_by       varchar(64)     default ''                 comment '更新者',
    dt_modified       datetime                                   comment '更新时间',
    remark            varchar(500)    default null               comment '备注',
    deleted           tinyint         default '0'                comment '逻辑删除(0:未删除 1:已删除)',
    primary key (id),
    unique key (user_id),
    index account_idx(user_name, email, phone)
) engine=innodb auto_increment=1 comment = '用户信息表';
INSERT INTO sys_user (
    user_id, dept_id, user_name, nick_name, user_type, email, phone, sex, avatar,
    password, status, created_by, dt_created, remark
)
VALUES
-- 系统管理员用户
('admin001', 'dept001', 'admin', '系统管理员', '00',
'admin@example.com', '13800000001', 0, '/avatars/admin.jpg',
 '$2a$10$/6NBLlU3wvJcyn4tfV2e5Ow6OUAENMO644iOaGDCYP2LT/pDVursu', -- 密码: admin123
 1, 'system', NOW(), '系统超级管理员账号'),
('admin002', 'dept001', 'superadmin', '超级管理员', '00',
 'superadmin@example.com', '13800000002', 1, '/avatars/superadmin.jpg',
 '$2a$10$/6NBLlU3wvJcyn4tfV2e5Ow6OUAENMO644iOaGDCYP2LT/pDVursu', -- 密码: admin123
 1, 'system', NOW(), '系统超级管理员备用账号'),
-- 业务管理员用户
('manager001', 'dept002', 'manager', '业务经理', '10',
 'manager@example.com', '13800000003', 0, '/avatars/manager.jpg',
 '$2a$10$iw5rtW9TvNeKhkcDtaNm6.NopMDurhsKDHxJOzrocQhcdoqKotdYC', -- 密码: manager123
 1, 'admin001', NOW(), '产品业务管理员'),
('manager002', 'dept002', 'manager2', '业务主管', '10',
 'manager2@example.com', '13800000004', 1, '/avatars/manager2.jpg',
 '$2a$10$iw5rtW9TvNeKhkcDtaNm6.NopMDurhsKDHxJOzrocQhcdoqKotdYC', -- 密码: manager123
 1, 'admin001', NOW(), '产品业务主管'),
-- 普通用户
('user001', 'dept003', 'zhangsan', '张三', '20',
 'zhangsan@example.com', '13800000005', 0, '/avatars/zhangsan.jpg',
 '$2a$10$TjUaaWtkYhYknvGNrvMf3urWVI1LeLMd6Djcw5OURLyMZzvIX.tBq', -- 密码: user123
 1, 'manager001', NOW(), '普通员工用户'),
('user002', 'dept003', 'lisi', '李四', '20',
 'lisi@example.com', '13800000006', 1, '/avatars/lisi.jpg',
 '$2a$10$TjUaaWtkYhYknvGNrvMf3urWVI1LeLMd6Djcw5OURLyMZzvIX.tBq', -- 密码: user123
 1, 'manager001', NOW(), '普通员工用户'),
('user003', 'dept004', 'wangwu', '王五', '20',
 'wangwu@example.com', '13800000007', 0, '/avatars/wangwu.jpg',
 '$2a$10$TjUaaWtkYhYknvGNrvMf3urWVI1LeLMd6Djcw5OURLyMZzvIX.tBq', -- 密码: user123
 1, 'manager002', NOW(), '普通员工用户'),
-- 访客用户
('guest001', 'dept005', 'guest', '访客用户', '20',
 'guest@example.com', '13800000008', 2, '/avatars/guest.jpg',
 '$2a$10$IccEwf/fYkLYQ/1H9vEvB./o006kPSnb34ljWI5cbt8XmPLx1vx2S', -- 密码: guest123
 1, 'system', NOW(), '系统访客账号，仅供演示使用');

drop table if exists sys_user_openid;
create table sys_user_openid (
    id                bigint      not null auto_increment    comment '主键id',
    user_id           varchar(31)     not null                   comment '用户ID',
    openid            varchar(100)    not null                   comment '第三方账号openid',
    type              varchar(31)     not null                   comment '第三方账号类型',
    created_by        varchar(64)     default ''                 comment '创建者',
    dt_created        datetime        default current_timestamp  comment '创建时间',
    modified_by       varchar(64)     default ''                 comment '更新者',
    dt_modified       datetime                                   comment '更新时间',
    deleted           tinyint         default 0                  comment '逻辑删除(0:未删除 1:已删除)',
    primary key (id),
    index user_idx(user_id, openid)
) engine=innodb auto_increment=1 comment = '用户openid表';

drop table if exists tenant;
create table tenant (
    id                bigint          not null auto_increment    comment '主键id',
    tenant_id         varchar(31)     not null                   comment '租户ID',
    tenant_name       varchar(30)     not null                   comment '租户名称',
    created_by        varchar(64)     default ''                 comment '创建者',
    dt_created        datetime        default current_timestamp  comment '创建时间',
    modified_by       varchar(64)     default ''                 comment '更新者',
    dt_modified       datetime                                   comment '更新时间',
    deleted           tinyint         default 0                  comment '逻辑删除(0:未删除 1:已删除)',
    primary key (id),
    unique key (tenant_id)
) engine=innodb auto_increment=1 comment = '租户表';

drop table if exists  menu;
create table menu (
                          id                bigint          not null auto_increment    comment '主键ID',
                          menu_id           varchar(50)     not null                   comment '菜单唯一标识',
                          parent_menu_id    varchar(50)     default null               comment '父级菜单ID',
                          name              varchar(100)    not null                   comment '路由名称(必须唯一)',
                          path              varchar(200)    not null                   comment '路由路径',
                          component         varchar(200)    default null               comment '组件路径(字符串)',
                          redirect          varchar(200)    default null               comment '重定向路径',
                          menu_type         varchar(20)     not null                   comment '菜单类型(catalog/menu/button/embedded/link)',
                          status            tinyint         default 1                  comment '状态(0:禁用 1:启用)',
                          sort_order        int             default 0                  comment '排序号',
                          created_by        varchar(64)     default ''                 comment '创建者',
                          dt_created        datetime        default current_timestamp  comment '创建时间',
                          modified_by       varchar(64)     default ''                 comment '更新者',
                          dt_modified       datetime        default null               comment '更新时间',
                          deleted           tinyint         default 0                  comment '逻辑删除(0:未删除 1:已删除)',
                          primary key (id),
                          unique key uk_name (name),
                          key idx_menu_id (menu_id, parent_menu_id, menu_type),
                          key idx_status (status)
) engine=innodb auto_increment=1 comment = '系统菜单表';

-- 仪表板菜单
INSERT INTO menu (menu_id, parent_menu_id, name, path, component, redirect, menu_type, status, sort_order, created_by,
                  dt_created)
VALUES ('dashboard', NULL, 'Dashboard', '/dashboard', NULL, '/analytics', 'catalog', 1, -1, 'system', NOW()),
       ('analytics', 'dashboard', 'Analytics', '/analytics', '/dashboard/analytics/index', NULL, 'menu', 1, 1, 'system',
        NOW()),
       ('workspace', 'dashboard', 'Workspace', '/workspace', '/dashboard/workspace/index', NULL, 'menu', 1, 2, 'system',
        NOW());

-- 产品管理菜单
INSERT INTO menu (menu_id, parent_menu_id, name, path, component, redirect, menu_type, status, sort_order, created_by,
                  dt_created)
VALUES ('product', NULL, 'ProductManagement', '/product', NULL, NULL, 'catalog', 1, 2, 'system', NOW()),
       ('service-catalog', 'product', 'ServiceCatalog', '/product/service-catalog', '/product/service-catalog/index',
        NULL, 'menu', 1, 1, 'system', NOW()),
       ('sales-pool', 'product', 'SalesPool', '/product/sales-pool', '/product/sales-pool/index', NULL, 'menu', 1, 2,
        'system', NOW());

-- 系统管理菜单
INSERT INTO menu (menu_id, parent_menu_id, name, path, component, redirect, menu_type, status, sort_order, created_by,
                  dt_created)
VALUES ('system', NULL, 'System', '/system', NULL, NULL, 'catalog', 1, 9997, 'system', NOW()),
       ('system-menu', 'system', 'SystemMenu', '/system/menu', '/system/menu/index', NULL, 'menu', 1, 1, 'system',
        NOW()),
       ('system-dept', 'system', 'SystemDept', '/system/dept', '/system/dept/index', NULL, 'menu', 1, 2, 'system',
        NOW()),
       ('system-role', 'system', 'SystemRole', '/system/role', '/system/role/index', NULL, 'menu', 1, 3, 'system',
        NOW()),
       ('system-tenant', 'system', 'SystemTenant', '/system/tenant', '/system/tenant/index', NULL, 'menu', 1, 4, 'system',
        NOW());

-- 系统管理按钮权限
INSERT INTO menu (menu_id, parent_menu_id, name, path, component, redirect, menu_type, status, sort_order, created_by,
                  dt_created)
VALUES ('system-menu-create', 'system-menu', 'SystemMenuCreate', '', NULL, NULL, 'button', 1, 1, 'system', NOW()),
       ('system-menu-edit', 'system-menu', 'SystemMenuEdit', '', NULL, NULL, 'button', 1, 2, 'system', NOW()),
       ('system-menu-delete', 'system-menu', 'SystemMenuDelete', '', NULL, NULL, 'button', 1, 3, 'system', NOW()),
       ('system-dept-create', 'system-dept', 'SystemDeptCreate', '', NULL, NULL, 'button', 1, 1, 'system', NOW()),
       ('system-dept-edit', 'system-dept', 'SystemDeptEdit', '', NULL, NULL, 'button', 1, 2, 'system', NOW()),
       ('system-dept-delete', 'system-dept', 'SystemDeptDelete', '', NULL, NULL, 'button', 1, 3, 'system', NOW());

-- 演示菜单 (根据不同角色)
INSERT INTO menu (menu_id, parent_menu_id, name, path, component, redirect, menu_type, status, sort_order, created_by,
                  dt_created)
VALUES ('demos', NULL, 'Demos', '/demos', NULL, NULL, 'catalog', 1, 1000, 'system', NOW()),
       ('demos-element', 'demos', 'DemosElement', '/demos/element', '/demos/element/index', NULL, 'menu', 1, 10, 'system', NOW()),
       ('demos-form-basic', 'demos', 'DemosFormBasic', '/demos/form/basic', '/demos/form/basic', NULL, 'menu', 1, 20, 'system', NOW());



drop table if exists menu_meta;
create table menu_meta (
                               id                          bigint          not null auto_increment    comment '主键ID',
                               menu_id                     varchar(50)     not null                   comment '菜单ID',
                               title                       varchar(125)    not null                   comment '菜单标题',
                               icon                        varchar(125)    default null               comment '菜单图标',
                               active_icon                 varchar(125)    default null               comment '激活时图标',
                               active_path                 varchar(255)    default null               comment '激活路径',
                               authority                   varchar(255)    default null               comment '权限标识数组',
                               ignore_access               tinyint         default 0                  comment '忽略权限验证(0:否 1:是)',
                               menu_visible_with_forbidden tinyint         default 0                  comment '菜单可见但访问受限(0:否 1:是)',
                               hide_in_menu                tinyint         default 0                  comment '在菜单中隐藏(0:否 1:是)',
                               hide_in_tab                 tinyint         default 0                  comment '在标签页中隐藏(0:否 1:是)',
                               hide_in_breadcrumb          tinyint         default 0                  comment '在面包屑中隐藏(0:否 1:是)',
                               hide_children_in_menu       tinyint         default 0                  comment '隐藏子菜单(0:否 1:是)',
                               affix_tab                   tinyint         default 0                  comment '固定标签页(0:否 1:是)',
                               affix_tab_order             int             default 0                  comment '固定标签页顺序',
                               max_num_of_open_tab         int             default -1                 comment '最大打开标签数',
                               keep_alive                  tinyint         default 0                  comment '页面缓存(0:否 1:是)',
                               no_basic_layout             tinyint         default 0                  comment '不使用基础布局(0:否 1:是)',
                               link                        varchar(511)    default null               comment '外链地址',
                               iframe_src                  varchar(511)    default null               comment 'iframe地址',
                               open_in_new_window          tinyint         default 0                  comment '新窗口打开(0:否 1:是)',
                               badge                       varchar(50)     default null               comment '徽标内容',
                               badge_type                  varchar(20)     default null               comment '徽标类型(dot/normal)',
                               badge_variants              varchar(20)     default null               comment '徽标颜色(default/destructive/primary/success/warning)',
                               query_params                varchar(255)    default null               comment '路由查询参数',
                               created_by                  varchar(64)     default ''                 comment '创建者',
                               dt_created                  datetime        default current_timestamp  comment '创建时间',
                               modified_by                 varchar(64)     default ''                 comment '更新者',
                               dt_modified                 datetime        default null               comment '更新时间',
                               deleted                     tinyint         default 0                  comment '逻辑删除(0:未删除 1:已删除)',
                               primary key (id),
                               unique key uk_menu_id (menu_id)
) engine=innodb auto_increment=1 comment = '系统菜单元数据表';

-- 仪表板菜单 meta
INSERT INTO menu_meta (menu_id, title, icon, authority, hide_in_menu, affix_tab, created_by, dt_created)
VALUES ('dashboard', 'page.dashboard.title', NULL, NULL, 0, 0, 'system', NOW()),
       ('analytics', 'page.dashboard.analytics', NULL, 'dashboard:analytics', 0, 1, 'system', NOW()),
       ('workspace', 'page.dashboard.workspace', 'carbon:workspace', 'dashboard:workspace', 0, 1, 'system', NOW());

-- 产品管理菜单 meta
INSERT INTO menu_meta (menu_id, title, icon, authority, hide_in_menu, created_by, dt_created)
VALUES ('product', '产品管理', 'carbon:product', NULL, 0, 'system', NOW()),
       ('service-catalog', '服务目录', 'carbon:catalog', 'product:service-catalog', 0, 'system', NOW()),
       ('sales-pool', '售卖池管理', 'carbon:shopping-cart', 'product:sales-pool', 0, 'system', NOW());

-- 系统管理菜单 meta
INSERT INTO menu_meta (menu_id, title, icon, authority, badge, badge_type, badge_variants, hide_in_menu, created_by,
                       dt_created)
VALUES ('system', 'page.system.title', 'carbon:settings', NULL, 'new', 'normal', 'primary', 0, 'system', NOW()),
       ('system-menu', 'page.system.menu', 'carbon:menu', 'system:menu', NULL, NULL, NULL, 0, 'system', NOW()),
       ('system-dept', 'page.system.dept', 'carbon:tree-view-alt', 'system:dept', NULL, NULL, NULL, 0, 'system',
        NOW()),
       ('system-role', 'page.system.role', 'carbon:user-role', 'system:role', NULL, NULL, NULL, 0, 'system',
        NOW()),
       ('system-tenant', 'page.system.tenant', 'carbon:user', 'system:tenant', NULL, NULL, NULL, 0, 'system', NOW());

-- 系统管理按钮权限 meta
INSERT INTO menu_meta (menu_id, title, authority, hide_in_menu, created_by, dt_created)
VALUES ('system-menu-create', 'common.create', 'system:menu:create', 1, 'system', NOW()),
       ('system-menu-edit', 'common.edit', 'system:menu:edit', 1, 'system', NOW()),
       ('system-menu-delete', 'common.delete', 'system:menu:delete', 1, 'system', NOW()),
       ('system-dept-create', 'common.create', 'system:dept:create', 1, 'system', NOW()),
       ('system-dept-edit', 'common.edit', 'system:dept:edit', 1, 'system', NOW()),
       ('system-dept-delete', 'common.delete', 'system:dept:delete', 1, 'system', NOW());

-- 演示菜单 meta
INSERT INTO menu_meta (menu_id, title, icon, authority, hide_in_menu, created_by, dt_created)
VALUES ('demos', 'page.demos.title', 'carbon:chemistry', NULL, 0, 'system', NOW()),
       ('demos-element', 'Element 组件演示', 'lucide:layers', 'demos:element', 0,  'system', NOW()),
       ('demos-form-basic', '基础表单', 'lucide:edit-3', 'demos:form:basic', 0, 'system', NOW());

drop table if exists sys_role;
create table sys_role (
                          id                int          not null auto_increment    comment '主键ID',
                          role_id           varchar(50)     not null                   comment '角色ID',
                          role_name         varchar(100)    not null                   comment '角色名称',
                          description       varchar(500)    default null               comment '角色描述',
                          status            tinyint         default 1                  comment '状态(0:禁用 1:启用)',
                          created_by        varchar(64)     default ''                 comment '创建者',
                          dt_created        datetime        default current_timestamp  comment '创建时间',
                          modified_by       varchar(64)     default ''                 comment '更新者',
                          dt_modified       datetime                                   comment '更新时间',
                          deleted           tinyint         default 0                  comment '逻辑删除(0:未删除 1:已删除)',
                          primary key (id),
                          unique key (role_id)
) engine=innodb auto_increment=1 comment = '系统角色表';
-- 1. 角色数据
INSERT INTO sys_role (role_id, role_name, description, status, created_by, dt_created)
VALUES ('admin', '系统管理员',  '系统管理员角色，拥有所有权限，包括系统管理功能', 1, 'system', NOW()),
       ('manager', '业务管理员',  '业务管理员角色，拥有产品管理和部分系统功能权限', 1, 'system', NOW()),
       ('user', '普通用户',  '普通用户角色，拥有基础的查看和操作权限', 1, 'system', NOW()),
       ('guest', '访客用户',  '访客用户角色，只能查看仪表板和基础信息', 1, 'system', NOW());

-- 4. 创建用户角色关联表
drop table if exists sys_user_role;
create table sys_user_role (
                               id                bigint          not null auto_increment    comment '主键ID',
                               user_id           varchar(50)     not null                   comment '用户ID',
                               role_id           varchar(50)     not null                   comment '角色ID',
                               created_by        varchar(64)     default ''                 comment '创建者',
                               dt_created        datetime        default current_timestamp  comment '创建时间',
                               primary key (id),
                               unique key uk_user_role (user_id, role_id)
) engine=innodb auto_increment=1 comment = '用户角色关联表';

INSERT INTO sys_user_role (user_id, role_id, created_by, dt_created) VALUES
-- 超级管理员用户
('admin001', 'admin', 'system', NOW()),
('admin002', 'admin', 'system', NOW()),
-- 业务管理员用户
('manager001', 'manager', 'system', NOW()),
('manager002', 'manager', 'system', NOW()),
-- 普通用户
('user001', 'user', 'system', NOW()),
('user002', 'user', 'system', NOW()),
('user003', 'user', 'system', NOW()),
-- 访客用户
('guest001', 'guest', 'system', NOW());

-- 5. 创建角色菜单关联表
drop table if exists sys_role_menu;
create table sys_role_menu (
                               id                bigint          not null auto_increment    comment '主键ID',
                               role_id           varchar(50)     not null                   comment '角色ID',
                               menu_id           varchar(50)     not null                   comment '菜单ID',
                               created_by        varchar(64)     default ''                 comment '创建者',
                               dt_created        datetime        default current_timestamp  comment '创建时间',
                               primary key (id),
                               unique key uk_role_menu (role_id, menu_id)
) engine=innodb auto_increment=1 comment = '角色菜单关联表';
-- =============================================
-- admin 角色：系统管理员 - 拥有所有菜单权限
-- =============================================
INSERT INTO sys_role_menu (role_id, menu_id, created_by, dt_created) VALUES
-- 仪表板模块
('admin', 'dashboard', 'system', NOW()),
('admin', 'analytics', 'system', NOW()),
('admin', 'workspace', 'system', NOW()),

-- 产品管理模块
('admin', 'product', 'system', NOW()),
('admin', 'service-catalog', 'system', NOW()),
('admin', 'sales-pool', 'system', NOW()),

-- 系统管理模块
('admin', 'system', 'system', NOW()),
('admin', 'system-menu', 'system', NOW()),
('admin', 'system-dept', 'system', NOW()),
('admin', 'system-role', 'system', NOW()),
('admin', 'system-tenant', 'system', NOW()),

-- 系统管理按钮权限
('admin', 'system-menu-create', 'system', NOW()),
('admin', 'system-menu-edit', 'system', NOW()),
('admin', 'system-menu-delete', 'system', NOW()),
('admin', 'system-dept-create', 'system', NOW()),
('admin', 'system-dept-edit', 'system', NOW()),
('admin', 'system-dept-delete', 'system', NOW()),

-- 演示模块
('admin', 'demos', 'system', NOW()),
-- admin 角色
('admin', 'demos-element', 'system', NOW()),
('admin', 'demos-form-basic', 'system', NOW());


-- =============================================
-- manager 角色：业务管理员 - 拥有业务管理权限
-- =============================================
INSERT INTO sys_role_menu (role_id, menu_id, created_by, dt_created) VALUES
-- 仪表板模块
('manager', 'dashboard', 'system', NOW()),
('manager', 'analytics', 'system', NOW()),
('manager', 'workspace', 'system', NOW()),

-- 产品管理模块（完整权限）
('manager', 'product', 'system', NOW()),
('manager', 'service-catalog', 'system', NOW()),
('manager', 'sales-pool', 'system', NOW()),

-- 系统管理模块（部分权限，只能查看用户和部门）
('manager', 'system', 'system', NOW()),
('manager', 'system-dept', 'system', NOW()),
('manager', 'system-user', 'system', NOW()),

-- 演示模块
('manager', 'demos', 'system', NOW()),
('manager', 'demos-element', 'system', NOW()),
('manager', 'demos-form-basic', 'system', NOW());

-- =============================================
-- user 角色：普通用户 - 拥有基础查看权限
-- =============================================
INSERT INTO sys_role_menu (role_id, menu_id, created_by, dt_created) VALUES
-- 仪表板模块
('user', 'dashboard', 'system', NOW()),
('user', 'analytics', 'system', NOW()),
('user', 'workspace', 'system', NOW()),

-- 产品管理模块（只能查看服务目录）
('user', 'product', 'system', NOW()),
('user', 'service-catalog', 'system', NOW()),

-- 演示模块
('user', 'demos', 'system', NOW()),
('user', 'demos-element', 'system', NOW()),
('user', 'demos-form-basic', 'system', NOW());

-- =============================================
-- guest 角色：访客用户 - 只能查看仪表板
-- =============================================
INSERT INTO sys_role_menu (role_id, menu_id, created_by, dt_created) VALUES
-- 仪表板模块（只能查看分析页）
('guest', 'dashboard', 'system', NOW()),
('admin', 'demos', 'system', NOW()),
('guest', 'analytics', 'system', NOW()),
('guest', 'demos-element', 'system', NOW());
