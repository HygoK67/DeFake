-- 创建项目中各个实体的数据表的代码
-- sudo docker run -it --rm mysql mysql -h192.168.1.120 -uroot -p
-- 密码 testpasswd

CREATE TABLE users (                       -- 用户表
    -- 基本字段
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 自增主键
    user_name VARCHAR(50) NOT NULL,        -- 必须填写用户名
    email VARCHAR(255) UNIQUE,             -- 可以不填写邮箱，不能重复
    phone VARCHAR(20) NOT NULL UNIQUE,     -- 必须填写手机号，不能重复
    password_hash VARCHAR(255) NOT NULL,   -- 用户密码
    avatar_path VARCHAR(255) NULL,         -- 用户头像存储位置 (可能是网络 url)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 用户创建时间
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- 用户更新个人信息的时间
    last_login_at TIMESTAMP NULL,
    
    -- 业务扩展字段
    role ENUM('user', 'admin') DEFAULT 'user', -- 用户身份: 普通用户(包含组织管理员) 或者 root 用户
    group_id BIGINT NULL,                      -- 用户属于的组织

    foreign key (group_id) references groups(id)

);

CREATE TABLE papers (                      -- 论文表，主要存储元文件
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  
    title VARCHAR(255) NOT NULL,           -- 论文标题
    abstract TEXT NULL,                    -- 论文摘要
    doi VARCHAR(100) UNIQUE NULL,          -- 论文 doi 号
    published_at date NULL,                -- 论文发表日期
    file_path VARCHAR(255) NOT NULL,       -- 论文文件的存储位置 (可能是网络 URL)
    page_num  INT NULL,                    -- 论文页面数量
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 条目创建时间
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 条目更新时间
);

CREATE TABLE figures (                     -- 图像表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 唯一id
    paper_id BIGINT NULL,                  -- 该图像属于的论文的id，可以为空
    uploader_id BIGINT NOT NULL,           -- 上传该图像的用户的用户id, 不能为空
    foreign key (paper_id) references papers(id),
    foreign key (uploader_id) references users(id),
    file_path VARCHAR(255) NOT NULL,       -- 图像文件的存储位置 (可能是网络 URL)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 条目创建时间
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 条目更新时间
);

CREATE TABLE groups (                      -- 组织表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 组织唯一 id
    group_name VARCHAR(50) NOT NULL,       -- 组织名称
    group_leader_id BIGINT NOT NULL,       -- 组织领导者的用户 id
    foreign key (group_leader_id) references users(user_id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 条目创建时间
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP -- 条目更新时间
);

CREATE TABLE notification_templates (       -- 通知模板表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    template_code VARCHAR(255) NOT NULL,    -- 模板代号, 例如 USER_REGISTER, USER_LOGIN, DETECTION_FAILED 这类的
    title_template VARCHAR(255) NOT NULL,   -- 通知标题模板
    content_template TEXT NOT NULL,         -- 通知内容模板
    type ENUM('system', 'user') NOT NULL,   -- 通知类型
    priority ENUM('low', 'normal', 'high') DEFAULT 'normal' -- 通知优先级
);

CREATE TABLE notifications (                -- 通知记录表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,   -- 唯一 id
    template_id INT NULL,                   -- 可以使用模板也可以不使用
    user_id BIGINT NULL,                    -- 要发送给的用户 id
    title VARCHAR(255) NOT NULL,            -- 通知真正的标题, 可能由模板生成
    content TEXT NOT NULL,                  -- 通知真正的内容, 可能由模板生成
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- 通知产生的时间
    read_at TIMESTAMP NULL,                       -- 用户阅读该通知的时间
    FOREIGN KEY (template_id) REFERENCES notification_templates(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE operation_logs (              -- 操作日志表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 唯一 id
    user_id BIGINT NULL,                   -- 用户 id
    operation_type VARCHAR(50) NOT NULL,   -- 用户操作类型 (种类太多，因此不使用 ENUM 进行存储)
    operation_detail JSON NOT NULL,        -- 操作细节说明 (可能需要一个 json 来存储结构化的信息)
    ip_address VARCHAR(45) NOT NULL,       -- 用户此次操作的 ip 地址
    status ENUM('success', 'failed') NOT NULL, -- 操作是否成功
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 操作时间
    foreign key user_id references users(id),
    INDEX idx_user_time (user_id, created_at), -- 第一个索引 (按照用户和操作时间进行索引)
    INDEX idx_type (operation_type)            -- 第二个索引 (按照操作类型进行索引)
)