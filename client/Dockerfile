FROM node:20-bookworm-slim

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .

EXPOSE 3100

CMD ["npm", "run", "dev-server", "--", "--host", "0.0.0.0"]
