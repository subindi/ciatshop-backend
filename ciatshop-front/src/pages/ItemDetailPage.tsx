import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { itemApi, Item } from '../api/client';

export default function ItemDetailPage() {
  const { itemCd } = useParams<{ itemCd: string }>();
  const [item, setItem] = useState<Item | null>(null);

  useEffect(() => {
    if (!itemCd) return;
    itemApi.getOne(itemCd).then(res => {
      if (res.success && res.data) setItem(res.data);
    });
  }, [itemCd]);

  if (!item) return <p style={{ padding: 32 }}>로딩 중...</p>;

  return (
    <div style={{ maxWidth: 600, margin: '32px auto', padding: '0 16px' }}>
      <h2 style={{ marginBottom: 20 }}>상품 상세</h2>
      <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: 14 }}>
        <tbody>
          <Row label="상품코드" value={item.itemCd} />
          <Row label="상품명" value={item.itemNm} />
          <Row label="재고수량" value={item.itemQnty.toLocaleString()} />
          <Row label="원가" value={item.unitPrice != null ? item.unitPrice.toLocaleString() + '원' : '-'} />
          <Row label="판매가" value={item.sellPrice != null ? item.sellPrice.toLocaleString() + '원' : '-'} />
          <Row label="상품구분" value={item.itemSeCd ?? '-'} />
          <Row label="카테고리" value={item.itemCateCd ?? '-'} />
        </tbody>
      </table>
      <div style={{ marginTop: 20 }}>
        <Link to="/items" style={{ padding: '8px 16px', background: '#95a5a6', color: '#fff', borderRadius: 4, textDecoration: 'none' }}>
          목록
        </Link>
      </div>
    </div>
  );
}

function Row({ label, value }: { label: string; value: string }) {
  return (
    <tr>
      <td style={{ padding: '10px 14px', background: '#f8f9fa', fontWeight: 600, width: 160, borderBottom: '1px solid #eee' }}>{label}</td>
      <td style={{ padding: '10px 14px', borderBottom: '1px solid #eee' }}>{value}</td>
    </tr>
  );
}
