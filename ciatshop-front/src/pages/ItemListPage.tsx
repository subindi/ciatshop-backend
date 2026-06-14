import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { itemApi, Item } from '../api/client';

export default function ItemListPage() {
  const [items, setItems] = useState<Item[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    itemApi.getAll().then(res => {
      if (res.success && res.data) setItems(res.data);
    }).finally(() => setLoading(false));
  }, []);

  if (loading) return <p style={{ padding: 32 }}>로딩 중...</p>;

  return (
    <div style={{ maxWidth: 900, margin: '32px auto', padding: '0 16px' }}>
      <h2 style={{ marginBottom: 20 }}>상품 목록</h2>
      {items.length === 0 ? (
        <p>등록된 상품이 없습니다.</p>
      ) : (
        <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
          <thead>
            <tr style={{ background: '#f0f0f0' }}>
              <th style={th}>상품코드</th>
              <th style={th}>상품명</th>
              <th style={th}>재고</th>
              <th style={th}>판매가</th>
              <th style={th}>구분</th>
            </tr>
          </thead>
          <tbody>
            {items.map(item => (
              <tr key={item.itemCd}>
                <td style={td}>
                  <Link to={`/items/${item.itemCd}`} style={{ color: '#3498db', fontWeight: 600 }}>
                    {item.itemCd}
                  </Link>
                </td>
                <td style={td}>{item.itemNm}</td>
                <td style={td}>{item.itemQnty.toLocaleString()}</td>
                <td style={td}>{item.sellPrice != null ? item.sellPrice.toLocaleString() + '원' : '-'}</td>
                <td style={td}>{item.itemSeCd ?? '-'}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}

const th: React.CSSProperties = { padding: '10px 12px', textAlign: 'left', fontWeight: 600 };
const td: React.CSSProperties = { padding: '10px 12px', borderBottom: '1px solid #eee' };
