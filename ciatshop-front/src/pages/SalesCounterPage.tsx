import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { Item, Sales, SalesEvent, itemApi, salesApi, salesEventApi } from '../api/client';

interface FloatEffect {
  id: number;
  value: number;
  itemNm: string;
  topPct: number;
}

export default function SalesCounterPage() {
  const { eventSeq, ymd } = useParams<{ eventSeq: string; ymd: string }>();
  const navigate = useNavigate();
  const parsedEventSeq = eventSeq ? Number(eventSeq) : null;

  const [event, setEvent] = useState<SalesEvent | null>(null);
  const [items, setItems] = useState<Item[]>([]);
  const [counts, setCounts] = useState<Record<string, number>>({});
  const [effects, setEffects] = useState<FloatEffect[]>([]);
  const [loading, setLoading] = useState(true);

  const effectIdRef = useRef(0);
  const countsRef = useRef<Record<string, number>>({});
  const pendingDeltaRef = useRef<Record<string, number>>({});
  const debounceTimers = useRef<Record<string, ReturnType<typeof setTimeout>>>({});

  const applyServerCounts = (data: Sales[]) => {
    const c: Record<string, number> = {};
    data.forEach(s => { c[s.itemCd] = (c[s.itemCd] ?? 0) + s.salesQnty; });
    countsRef.current = c;
    setCounts({ ...c });
  };

  const refreshCounts = useCallback(async () => {
    if (!ymd || Object.keys(pendingDeltaRef.current).length > 0) return;
    try {
      const res = await salesApi.getByDate(ymd, parsedEventSeq);
      if (res.success && res.data) applyServerCounts(res.data);
    } catch {
      // 폴링 실패는 무음 처리
    }
  }, [ymd, parsedEventSeq]);

  useEffect(() => {
    if (!eventSeq || !ymd) return;

    salesEventApi.getOne(Number(eventSeq)).then(res => {
      if (res.success && res.data) setEvent(res.data);
    }).catch(() => {});

    itemApi.getAll().then(res => {
      if (res.success && res.data) setItems(res.data);
    }).catch(() => {});

    salesApi.getByDate(ymd, parsedEventSeq).then(res => {
      if (res.success && res.data) applyServerCounts(res.data);
      else alert(`수량 조회 실패: ${res.message ?? JSON.stringify(res)}`);
    }).catch(err => {
      alert(`수량 조회 오류: ${err?.message ?? err}`);
    }).finally(() => {
      setLoading(false);
    });

  }, [eventSeq, ymd, parsedEventSeq]);

  useEffect(() => {
    const t = setInterval(refreshCounts, 5000);
    return () => clearInterval(t);
  }, [refreshCounts]);

  const totalCount = Object.values(counts).reduce((a, b) => a + b, 0);

  const spawnEffect = (value: number, itemNm: string, buttonY: number) => {
    const topPct = buttonY / window.innerHeight < 0.5 ? 68 : 28;
    const id = ++effectIdRef.current;
    setEffects(prev => [...prev, { id, value, itemNm, topPct }]);
    setTimeout(() => setEffects(prev => prev.filter(ef => ef.id !== id)), 1100);
  };

  const flushAdjust = useCallback(async (item: Item) => {
    const accDelta = pendingDeltaRef.current[item.itemCd];
    if (accDelta === undefined) return;
    delete pendingDeltaRef.current[item.itemCd];

    try {
      const res = await salesApi.adjust({
        itemCd: item.itemCd,
        salesYmd: ymd!,
        salesEventSeq: parsedEventSeq,
        delta: accDelta,
      });

      if (res.success && res.data) {
        countsRef.current = { ...countsRef.current, [item.itemCd]: res.data.salesQnty };
        setCounts({ ...countsRef.current });
      } else {
        alert(`저장 실패: ${res.message ?? '알 수 없는 오류'}`);
        await refreshCounts();
      }
    } catch (err) {
      alert(`통신 오류: ${err instanceof Error ? err.message : '서버 연결 실패'}`);
      await refreshCounts();
    }
  }, [ymd, parsedEventSeq, refreshCounts]);

  const handleAdjust = (item: Item, delta: number, e?: React.PointerEvent) => {
    const current = countsRef.current[item.itemCd] ?? 0;
    if (delta < 0 && current <= 0) return;
    const buttonY = e ? (e.currentTarget as HTMLElement).getBoundingClientRect().top + 13 : window.innerHeight / 2;
    spawnEffect(delta, item.itemNm, buttonY);

    const newCount = Math.max(0, current + delta);
    countsRef.current = { ...countsRef.current, [item.itemCd]: newCount };
    setCounts({ ...countsRef.current });

    pendingDeltaRef.current[item.itemCd] = (pendingDeltaRef.current[item.itemCd] ?? 0) + delta;
    clearTimeout(debounceTimers.current[item.itemCd]);
    debounceTimers.current[item.itemCd] = setTimeout(() => flushAdjust(item), 300);
  };

  if (loading) {
    return (
      <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh', color: '#888' }}>
        로딩 중...
      </div>
    );
  }

  const dateLabel = ymd ? `${ymd.slice(0, 4)}.${ymd.slice(4, 6)}.${ymd.slice(6, 8)}` : '';

  return (
    <div style={{ maxWidth: 480, margin: '0 auto', background: '#f0f2f5', minHeight: '100vh' }}>

      {/* 헤더 */}
      <div style={{
        background: '#1a252f', color: '#fff',
        padding: '8px 12px',
        position: 'sticky', top: 0, zIndex: 100,
        display: 'flex', alignItems: 'center', justifyContent: 'space-between',
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: 10, minWidth: 0 }}>
          <button
            onClick={() => navigate(`/counter/${eventSeq}`)}
            style={{ background: 'none', border: 'none', color: '#7f8c8d', fontSize: 11, cursor: 'pointer', padding: 0, flexShrink: 0 }}
          >
            ‹ 날짜
          </button>
          <div style={{ minWidth: 0 }}>
            <div style={{ fontSize: 12, fontWeight: 700, color: '#3498db', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
              {event?.salesEventNm}
            </div>
            <div style={{ fontSize: 10, color: '#95a5a6' }}>{dateLabel}</div>
          </div>
        </div>
        <div style={{ textAlign: 'right', flexShrink: 0, marginLeft: 8 }}>
          <div style={{ fontSize: 10, color: '#95a5a6' }}>총 판매</div>
          <div style={{ fontSize: 20, fontWeight: 900, color: '#3498db', lineHeight: 1 }}>
            {totalCount}<span style={{ fontSize: 11, marginLeft: 1 }}>개</span>
          </div>
        </div>
      </div>

      {/* 3열 그리드 */}
      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 5, padding: '6px 6px 20px' }}>
        {items.map(item => {
          const count = counts[item.itemCd] ?? 0;
          return (
            <div key={item.itemCd} style={{
              background: '#fff', borderRadius: 8,
              padding: '6px 7px',
              boxShadow: '0 1px 3px rgba(0,0,0,.08)',
              display: 'flex', flexDirection: 'column', gap: 5,
            }}>
              {/* 상품명 | 수량 — 1행 */}
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}>
                <span style={{
                  fontSize: 11, fontWeight: 600, color: '#2c3e50',
                  flex: 1, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap',
                  marginRight: 4,
                }}>
                  {item.itemNm}
                </span>
                <span style={{
                  fontSize: 18, fontWeight: 900, lineHeight: 1, flexShrink: 0,
                  color: count > 0 ? '#e74c3c' : '#ccc',
                  transition: 'color .15s',
                  minWidth: 18, textAlign: 'right',
                }}>
                  {count}
                </span>
              </div>

              {/* +1 +2 − — 3버튼 1행 */}
              <div style={{ display: 'grid', gridTemplateColumns: 'repeat(3, 1fr)', gap: 3 }}>
                {([1, 2] as const).map(n => (
                  <button
                    key={n}
                    onPointerDown={(e) => { e.preventDefault(); handleAdjust(item, n, e); }}
                    style={{
                      height: 26, borderRadius: 5, border: 'none',
                      background: n === 1 ? '#27ae60' : '#2980b9',
                      color: '#fff', fontSize: 12, fontWeight: 800,
                      cursor: 'pointer',
                      WebkitTapHighlightColor: 'transparent',
                      touchAction: 'manipulation',
                    }}
                  >
                    +{n}
                  </button>
                ))}
                <button
                  onPointerDown={(e) => { e.preventDefault(); handleAdjust(item, -1, e); }}
                  style={{
                    height: 26, borderRadius: 5, border: 'none',
                    background: count > 0 ? '#e74c3c' : '#ecf0f1',
                    color: count > 0 ? '#fff' : '#bdc3c7',
                    fontSize: 16, fontWeight: 700, lineHeight: 1,
                    cursor: count > 0 ? 'pointer' : 'default',
                    WebkitTapHighlightColor: 'transparent',
                    touchAction: 'manipulation',
                  }}
                >
                  −
                </button>
              </div>
            </div>
          );
        })}
      </div>

      {/* 플로팅 효과 — 화면 중앙 */}
      {effects.map(ef => (
        <div key={ef.id} style={{
          position: 'fixed',
          left: '50%', top: `${ef.topPct}%`,
          transform: 'translate(-50%, -50%)',
          pointerEvents: 'none', zIndex: 9999,
          animation: 'floatUp 1.1s cubic-bezier(0.22,1,0.36,1) forwards',
          display: 'flex', flexDirection: 'column', alignItems: 'center', gap: 6,
        }}>
          {/* 제품명 */}
          <div style={{
            fontSize: 42, fontWeight: 800,
            color: '#fff',
            textShadow: `0 2px 12px rgba(0,0,0,0.8), 0 0 20px ${ef.value > 0 ? 'rgba(46,204,113,0.9)' : 'rgba(231,76,60,0.9)'}`,
            WebkitTextStroke: '0.5px rgba(0,0,0,0.5)',
            whiteSpace: 'nowrap',
            letterSpacing: '0px',
          }}>
            {ef.itemNm}
          </div>
          {/* +N / -1 숫자 */}
          <div style={{
            fontSize: 110, fontWeight: 900, lineHeight: 1,
            color: ef.value > 0 ? '#2ecc71' : '#e74c3c',
            textShadow: `
              0 0 40px ${ef.value > 0 ? 'rgba(46,204,113,0.9)' : 'rgba(231,76,60,0.9)'},
              0 0 80px ${ef.value > 0 ? 'rgba(46,204,113,0.5)' : 'rgba(231,76,60,0.5)'},
              0 4px 0 ${ef.value > 0 ? '#1a7a43' : '#922b21'},
              0 8px 20px rgba(0,0,0,0.5)
            `,
            WebkitTextStroke: `3px ${ef.value > 0 ? '#1a7a43' : '#922b21'}`,
            letterSpacing: '-4px',
          }}>
            {ef.value > 0 ? `+${ef.value}` : ef.value}
          </div>
        </div>
      ))}

      <style>{`
        @keyframes floatUp {
          0%   { opacity:0; transform:translate(-50%, -50%) scale(0.3); }
          15%  { opacity:1; transform:translate(-50%, -50%) scale(1.3); }
          35%  { opacity:1; transform:translate(-50%, -55%) scale(1.0); }
          70%  { opacity:1; transform:translate(-50%, -70%) scale(0.95); }
          100% { opacity:0; transform:translate(-50%, -95%) scale(0.8); }
        }
        * { box-sizing:border-box; }
        button:active { opacity:.75; transform:scale(0.9); }
      `}</style>
    </div>
  );
}
