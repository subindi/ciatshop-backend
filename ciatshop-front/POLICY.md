# ciatshop-front 정책서

> 프로젝트: `ciatshop-front` (판매 카운터 — 핸드폰/태블릿용)  
> 포트: 80 (dev/prod 공통)  
> ciatshop API(8080) 실행 후 기동할 것

---

## 1. 기술 스택

| 항목 | 내용 |
|---|---|
| 언어 | TypeScript 5.x |
| 프레임워크 | React 18 |
| 빌드 도구 | Vite 5.x |
| 라우터 | React Router DOM 6.x |

---

## 2. 프로젝트 구조

```
ciatshop-front/
├── index.html
├── package.json
├── vite.config.ts          /api/* → localhost:8080 프록시, host: true (WiFi 접속)
├── tsconfig.json
├── nginx.conf              운영 배포용 (SPA 라우팅 + /api/ 프록시)
├── Dockerfile              멀티스테이지 빌드 (Node → nginx)
└── src/
    ├── main.tsx
    ├── App.tsx             React Router 라우트 정의
    ├── api/
    │   └── client.ts       타입 정의 + fetch 래퍼 + salesApi
    └── pages/
        ├── DateSelectPage.tsx    판매 날짜 선택
        ├── EventSelectPage.tsx   판매 이벤트 선택
        ├── SalesCounterPage.tsx  판매 수량 카운터 (메인)
        ├── ItemListPage.tsx      상품 목록
        └── ItemDetailPage.tsx    상품 상세
```

---

## 3. 라우트

| 경로 | 페이지 | 설명 |
|---|---|---|
| `/` | → `/date` 리다이렉트 | |
| `/date` | DateSelectPage | 판매 날짜 선택 |
| `/event` | EventSelectPage | 판매 이벤트 선택 |
| `/counter` | SalesCounterPage | 판매 수량 카운터 |
| `/items` | ItemListPage | 상품 목록 |
| `/items/:itemCd` | ItemDetailPage | 상품 상세 |

---

## 4. API 호출 방식

**개발 환경**: Vite proxy가 `/api/*` → `http://localhost:8080` 포워딩 (CORS 불필요)

```typescript
// vite.config.ts
proxy: { '/api': { target: 'http://localhost:8080', changeOrigin: true } }
```

**운영 환경**: nginx가 `/api/` → `http://api:8080/api/` 프록시 처리

```typescript
// src/api/client.ts
export const salesApi = {
  getByDate: (salesYmd: string, salesEventSeq?: number | null) => { ... },
  adjust: (data: SalesAdjustRequest) => { ... },
};
```

---

## 5. SalesCounterPage 핵심 구현

### 레이아웃
- 3열 그리드 (8행 목표 — 한 화면에 24개 상품)
- 상품명 + 수량 가로 배치, 버튼 `[+1][+2][−]`

### 디바운스 (300ms)
버튼 연타 시 delta를 누적했다가 300ms 후 단일 API 호출

```typescript
pendingDeltaRef.current[itemCd] += delta;
clearTimeout(debounceTimers.current[itemCd]);
debounceTimers.current[itemCd] = setTimeout(() => flushAdjust(item), 300);
```

### Float 이펙트
버튼 클릭 시 화면 중앙에 수량(+1/+2/-1)과 상품명을 크게 표시.  
버튼 위치에 따라 상/하 적응형 위치 결정:
- 버튼이 화면 상반부 → 이펙트는 하단(top 68%)
- 버튼이 화면 하반부 → 이펙트는 상단(top 28%)

### UPSERT 처리
`salesApi.adjust({ itemCd, salesYmd, salesEventSeq, delta })` →  
API에서 해당 조합 존재 시 UPDATE, 없으면 INSERT

---

## 6. WiFi(핸드폰) 접속

```typescript
// vite.config.ts
server: { host: true }   // 0.0.0.0 바인딩 → 같은 WiFi의 핸드폰에서 접속 가능
```

API CORS: `allowedOriginPatterns("*")` 설정으로 핸드폰 IP 허용

---

## 7. 실행

```bash
cd ciatshop-front
npm install
npm run dev
# 접속: http://localhost:80
# 핸드폰: http://{PC_IP}
```

---

## 8. 빌드 및 배포

```bash
npm run build   # dist/ 생성
```

```dockerfile
# Dockerfile — 멀티스테이지
FROM node:20-alpine AS build
RUN npm ci && npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
```
