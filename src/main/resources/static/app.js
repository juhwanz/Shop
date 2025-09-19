// app.js

// HTML 문서가 모두 로드되면 이 함수를 실행합니다.
document.addEventListener('DOMContentLoaded', function () {
    const productIdSpan = document.getElementById('product-id');
    const productNameSpan = document.getElementById('product-name');
    const productPriceSpan = document.getElementById('product-price');
    const stockQuantitySpan = document.getElementById('stock-quantity');
    const orderBtn = document.getElementById('order-btn');
    const logDiv = document.getElementById('log');

    // 테스트할 상품 ID (HTML에 있는 ID와 동일하게 설정)
    const productId = productIdSpan.textContent;

    // 로그를 화면에 추가하는 함수
    function addLog(message) {
        logDiv.innerHTML += `<div>[${new Date().toLocaleTimeString()}] ${message}</div>`;
        logDiv.scrollTop = logDiv.scrollHeight; // 항상 마지막 로그가 보이도록 스크롤
    }

    // 상품 정보를 서버에서 가져와 화면을 업데이트하는 함수
    async function fetchProductData() {
        try {
            const response = await fetch(`/api/products/${productId}`);
            if (!response.ok) {
                throw new Error(`상품 정보 조회 실패: ${response.status}`);
            }
            const product = await response.json();

            // 가져온 데이터로 화면 업데이트
            productNameSpan.textContent = product.name;
            productPriceSpan.textContent = product.price;
            stockQuantitySpan.textContent = product.stockQuantity;
            addLog(`상품 정보 로드 완료: ${product.name}, 재고: ${product.stockQuantity}`);

        } catch (error) {
            addLog(`오류 발생: ${error.message}`);
            console.error(error);
        }
    }

    // 주문 버튼 클릭 시 실행될 함수
    orderBtn.addEventListener('click', async () => {
        addLog("주문 요청 시작...");
        try {
            const orderRequest = {
                userId: 99, // 임의의 사용자 ID
                productId: productId,
                quantity: 1
            };

            const response = await fetch('/api/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderRequest)
            });

            const responseBody = await response.text();

            if (!response.ok) {
                // 4xx, 5xx 에러 모두 여기서 처리
                addLog(`<span style="color: red;">주문 실패: ${response.status} - ${responseBody}</span>`);
            } else {
                addLog(`<span style="color: green;">${responseBody}</span>`);
                // 주문 성공 후, 최신 재고 정보를 다시 가져옴
                await fetchProductData();
            }

        } catch (error) {
            addLog(`네트워크 오류 발생: ${error.message}`);
            console.error(error);
        }
    });

    // 페이지가 처음 로드될 때 상품 정보를 가져옵니다.
    fetchProductData();
});