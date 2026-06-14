export interface ApiResponse<T> {
  success: boolean;
  message?: string;
  data?: T;
}

export interface Item {
  itemCd: string;
  itemNm: string;
  itemQnty: number;
  unitPrice: number | null;
  sellPrice: number | null;
  itemSeCd: string | null;
  itemCateCd: string | null;
}

export interface SalesEvent {
  salesEventSeq: number;
  salesEventNm: string;
  salesEventStrYmd: string;
  salesEventEndYmd: string;
  profitAmt: number | null;
  profitSeCd: string | null;
  profitVatYn: string | null;
}

export interface Sales {
  salesSeq: number;
  salesYmd: string;
  itemCd: string;
  itemNm: string;
  salesQnty: number;
  salesPrice: number | null;
  orderSeCd: string | null;
  salesSeCd: string | null;
  salesPlaceSeCd: string | null;
  salesEventSeq: number | null;
}

async function request<T>(path: string, options?: RequestInit): Promise<ApiResponse<T>> {
  const res = await fetch(`/api/v1${path}`, {
    headers: { 'Content-Type': 'application/json' },
    ...options,
  });
  return res.json();
}

export const itemApi = {
  getAll: () => request<Item[]>('/items'),
  getOne: (itemCd: string) => request<Item>(`/items/${encodeURIComponent(itemCd)}`),
};

export const salesEventApi = {
  getAll: () => request<SalesEvent[]>('/sales-events'),
  getOne: (seq: number) => request<SalesEvent>(`/sales-events/${seq}`),
};

export interface SalesAdjustRequest {
  itemCd: string;
  salesYmd: string;
  salesEventSeq: number | null;
  delta: number;
}

export const salesApi = {
  getByDate: (salesYmd: string, salesEventSeq?: number | null) => {
    const base = `/sales?salesYmd=${salesYmd}`;
    const qs = salesEventSeq != null ? `&salesEventSeq=${salesEventSeq}` : '';
    return request<Sales[]>(base + qs);
  },
  getOne: (seq: number) => request<Sales>(`/sales/${seq}`),
  adjust: (data: SalesAdjustRequest) =>
    request<Sales>('/sales/adjust', { method: 'POST', body: JSON.stringify(data) }),
  delete: (seq: number) => request<void>(`/sales/${seq}`, { method: 'DELETE' }),
};
