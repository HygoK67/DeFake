-- 创建项目中各个实体的数据表的代码
-- sudo docker run -it --rm mysql mysql -h192.168.1.120 -uroot -p

CREATE TABLE users (                       -- 用户表
    -- 基本字段
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 自增主键
    user_name VARCHAR(50) NOT NULL,        -- 必须填写用户名
    email VARCHAR(255) UNIQUE,             -- 可以不填写邮箱，不能重复
    phone VARCHAR(20) NOT NULL UNIQUE,     -- 必须填写手机号，不能重复
    password_hash VARCHAR(255) NOT NULL,   -- 用户密码
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP NULL,
    
    -- 业务扩展字段
    role ENUM('user', 'admin') DEFAULT 'user',
    group_id BIGINT references 

);

CREATE TABLE groups (                      -- 组织表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 组织唯一 id
    group_name VARCHAR(50) NOT NULL,       -- 组织名称

)

CREATE TABLE operation_logs (              -- 操作日志表
    id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 唯一 id
    user_id BIGINT NULL,                   -- 用户 id
    operation_type VARCHAR(50) NOT NULL,   -- 用户操作类型 (种类太多，因此不使用 ENUM 进行存储)
    operation_detail JSON NOT NULL,        -- 操作细节说明 (可能需要一个 json 来存储结构化的信息)
    ip_address VARCHAR(45) NOT NULL,       -- 用户此次操作的 ip 地址
    status ENUM('success', 'failed') NOT NULL, -- 操作是否成功
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 操作时间
    INDEX idx_user_time (user_id, created_at), -- 第一个索引 (按照用户和操作时间进行索引)
    INDEX idx_type (operation_type)            -- 第二个索引 (按照操作类型进行索引)
    foreign key user_id references users(id)
)