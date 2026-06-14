import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SalesEvent, salesEventApi } from '../api/client';

export default function EventSelectPage() {
  const [events, setEvents] = useState<SalesEvent[]>([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    salesEventApi.getAll().then(res => {
      if (res.success && res.data) setEvents(res.data);
      setLoading(false);
    });
  }, []);

  const formatDate = (ymd: string) =>
    `${ymd.slice(0, 4)}.${ymd.slice(4, 6)}.${ymd.slice(6, 8)}`;

  const getDayCount = (str: string, end: string) => {
    const s = new Date(Number(str.slice(0,4)), Number(str.slice(4,6))-1, Number(str.slice(6,8)));
    const e = new Date(Number(end.slice(0,4)), Number(end.slice(4,6))-1, Number(end.slice(6,8)));
    return Math.round((e.getTime() - s.getTime()) / 86400000) + 1;
  };

  if (loading) {
    return (
      <div style={{ display:'flex', justifyContent:'center', alignItems:'center', height:'100vh', color:'#888' }}>
        로딩 중...
      </div>
    );
  }

  return (
    <div style={{ maxWidth: 480, margin: '0 auto', background: '#f0f2f5', minHeight: '100vh' }}>
      <div style={{ background: '#1a252f', color: '#fff', padding: '20px 20px 24px', position: 'sticky', top: 0, zIndex: 10 }}>
        <div style={{ fontSize: 20, fontWeight: 700 }}>판매 이벤트 선택</div>
        <div style={{ fontSize: 13, color: '#7f8c8d', marginTop: 4 }}>이벤트를 선택하세요</div>
      </div>

      <div style={{ padding: '12px 10px 40px' }}>
        {events.length === 0 && (
          <div style={{ textAlign: 'center', color: '#aaa', padding: '60px 20px', fontSize: 15 }}>
            등록된 이벤트가 없습니다
          </div>
        )}
        {events.map(ev => {
          const days = getDayCount(ev.salesEventStrYmd, ev.salesEventEndYmd);
          return (
            <button
              key={ev.salesEventSeq}
              onClick={() => navigate(`/counter/${ev.salesEventSeq}`)}
              style={{
                width: '100%', textAlign: 'left', background: '#fff',
                border: 'none', borderRadius: 16, padding: '18px 18px',
                marginBottom: 8, boxShadow: '0 2px 8px rgba(0,0,0,.06)',
                cursor: 'pointer', display: 'block',
                WebkitTapHighlightColor: 'transparent',
              }}
            >
              <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                <div style={{ flex: 1 }}>
                  <div style={{ fontSize: 17, fontWeight: 700, color: '#2c3e50', marginBottom: 6 }}>
                    {ev.salesEventNm}
                  </div>
                  <div style={{ fontSize: 13, color: '#7f8c8d' }}>
                    {formatDate(ev.salesEventStrYmd)} ~ {formatDate(ev.salesEventEndYmd)}
                  </div>
                </div>
                <div style={{ marginLeft: 12, flexShrink: 0, textAlign: 'right' }}>
                  <div style={{
                    display: 'inline-block', background: '#eaf4fb', color: '#2980b9',
                    borderRadius: 20, padding: '4px 12px', fontSize: 13, fontWeight: 600,
                  }}>
                    {days}일
                  </div>
                  <div style={{ fontSize: 20, color: '#bbb', marginTop: 6 }}>›</div>
                </div>
              </div>
            </button>
          );
        })}
      </div>
    </div>
  );
}
