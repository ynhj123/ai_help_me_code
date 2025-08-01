# 使用Node.js镜像作为基础镜像
FROM node:21-alpine

# 设置工作目录
WORKDIR /app

# 复制package.json和package-lock.json到容器中
COPY package*.json ./

# 安装依赖
RUN npm install

# 复制源代码到容器中
COPY . .

# 构建应用
RUN npm run build

# 使用nginx作为web服务器
FROM nginx:alpine

# 复制nginx配置文件
COPY nginx.conf /etc/nginx/nginx.conf

# 复制构建好的文件到nginx目录
COPY --from=0 /app/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 3000

# 启动nginx
CMD ["nginx", "-g", "daemon off;"]