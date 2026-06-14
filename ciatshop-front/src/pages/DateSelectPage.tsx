import { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { SalesEvent, salesEventApi } from '../api/client';

const DAYS_KO = ['일', '월', '화', '수', '목', '금', '토'];

function getDatesInRange(str: string, end: string): string[] {
  const dates: string[] = [];
  const s = new Date(Number(str.slice(0,4)), Number(str.slice(4,6))-1, Number(str.slice(6,8)));
  const e = new Date(Number(end.slice(0,4)), Number(end.slice(4,6))-1, Number(end.slice(6,8)));
  for (const d = new Date(s); d <= e; d.setDate(d.getDate() + 1)) {
    const y = d.getFullYear();
    const m = String(d.getMonth() + 1).padStart(2, '0');
    const dd = String(d.getDate()).padStart(2, '0');
    dates.push(`${y}${m}${dd}`);
  }
  return dates;
}

function getTodayYmd(): string {
  const d = new Date();
  return `${d.getFullYear()}${String(d.getMonth()+1).padStart(2,'0')}${String(d.getDate()).padStart(2,'0')}`;
}

export default function DateSelectPage() {
  const { eventSeq } = useParams<{ eventSeq: string }>();
  const [event, setEvent] = useState<SalesEvent | null>(null);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();
  const today = getTodayYmd();

  useEffect(() => {
    if (!eventSeq) return;
    salesEventApi.getOne(Number(eventSeq)).then(res => {
      if (res.success && res.data) setEvent(res.data);
      setLoading(false);
    });
  }, [eventSeq]);

  if (loading) {
    return (
      <div style={{ display:'flex', justifyContent:'center', alignItems:'center', height:'100vh', color:'#888' }}>
        로딩 중...
      </div>
    );
  }

  if (!event) {
    return (
      <div style={{ display:'flex', flexDirection:'column', justifyContent:'center', alignItems:'center', height:'100vh', gap:16 }}>
        <div style={{ color:'#e74c3c', fontSize:16 }}>이벤트를 찾을 수 없습니다</div>
        <button onClick={() => navigate('/counter')} style={{ padding:'10px 24px', borderRadius:8, border:'none', background:'#3498db', color:'#fff', cursor:'pointer' }}>
          돌아가기
        </button>
      </div>
    );
  }

  const dates = getDatesInRange(event.salesEventStrYmd, event.salesEventEndYmd);

  return (
    <div style={{ maxWidth: 480, margin: '0 auto', background: '#f0f2f5', minHeight: '100vh' }}>
      {/* 헤더 */}
      <div style={{ background: '#1a252f', color: '#fff', padding: '16px 20px 20px', position: 'sticky', top: 0, zIndex: 10 }}>
        <button
          onClick={() => navigate('/counter')}
          style={{ background:'none', border:'none', color:'#7f8c8d', fontSize:14, cursor:'pointer', padding:'0 0 8px', display:'flex', alignItems:'center', gap:4 }}
        >
          ‹ 이벤트 목록
        </button>
        <div style={{ fontSize: 18, fontWeight: 700 }}>{event.salesEventNm}</div>
        <div style={{ fontSize: 13, color: '#7f8c8d', marginTop: 4 }}>
          {event.salesEventStrYmd.slice(0,4)}.{event.salesEventStrYmd.slice(4,6)}.{event.salesEventStrYmd.slice(6,8)}
          {' ~ '}
          {event.salesEventEndYmd.slice(0,4)}.{event.salesEventEndYmd.slice(4,6)}.{event.salesEventEndYmd.slice(6,8)}
        </div>
      </div>

      {/* 날짜 선택 */}
      <div style={{ padding: '16px 10px 40px' }}>
        <div style={{ fontSize: 14, color: '#888', marginBottom: 12, paddingLeft: 4 }}>날짜를 선택하세요</div>
        {dates.map(ymd => {
          const d = new Date(Number(ymd.slice(0,4)), Number(ymd.slice(4,6))-1, Number(ymd.slice(6,8)));
          const dayStr = DAYS_KO[d.getDay()];
          const isToday = ymd === today;
          const isSat = d.getDay() === 6;
          const isSun = d.getDay() === 0;

          return (
            <button
              key={ymd}
              onClick={() => navigate(`/counter/${eventSeq}/${ymd}`)}
              style={{
                width: '100%', textAlign: 'left', border: 'none', borderRadius: 14,
                padding: '16px 20px', marginBottom: 8, cursor: 'pointer',
                background: isToday ? '#2980b9' : '#fff',
                boxShadow: isToday ? '0 4px 12px rgba(41,128,185,.3)' : '0 2px 6px rgba(0,0,0,.06)',
                display: 'flex', alignItems: 'center', justifyContent: 'space-between',
                WebkitTapHighlightColor: 'transparent',
              }}
            >
              <div style={{ display: 'flex', alignItems: 'baseline', gap: 10 }}>
                <span style={{ fontSize: 22, fontWeight: 800, color: isToday ? '#fff' : isSun ? '#e74c3c' : isSat ? '#3498db' : '#2c3e50' }}>
                  {ymd.slice(4,6)}/{ymd.slice(6,8)}
                </span>
                <span style={{ fontSize: 15, fontWeight: 600, color: isToday ? 'rgba(255,255,255,.8)' : isSun ? '#e74c3c' : isSat ? '#3498db' : '#888' }}>
                  ({dayStr})
                </span>
                {isToday && (
                  <span style={{ fontSize: 12, background: 'rgba(255,255,255,.25)', color: '#fff', borderRadius: 10, padding: '2px 8px', fontWeight: 600 }}>
                    오늘
                  </span>
                )}
              </div>
              <span style={{ fontSize: 22, color: isToday ? 'rgba(255,255,255,.6)' : '#ccc' }}>›</span>
            </button>
          );
        })}
      </div>
    </div>
  );
}
