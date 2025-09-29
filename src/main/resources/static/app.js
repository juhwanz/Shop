document.addEventListener('DOMContentLoaded', function () {
    // --- HTML 요소 가져오기 ---
    const productListBody = document.getElementById('product-list');
    const productForm = document.getElementById('product-form');
    const logDiv = document.getElementById('log');
    const productIdInput = document.getElementById('product-id');
    const nameInput = document.getElementById('name');
    const priceInput = document.getElementById('price');
    const stockQuantityInput = document.getElementById('stockQuantity');
    const cancelBtn = document.getElementById('cancel-btn');

    const API_BASE_URL = '/api';

    // --- 핵심 함수들 ---

    // 로그를 화면에 추가하는 함수
    function addLog(message, isError = false) {
        const logMessage = document.createElement('div');
        const timestamp = new Date().toLocaleTimeString();
        logMessage.innerHTML = `[${timestamp}] ${message}`;
        if (isError) logMessage.style.color = 'red';
        logDiv.appendChild(logMessage);
        logDiv.scrollTop = logDiv.scrollHeight;
    }

    // 상품 목록을 서버에서 불러와 화면 테이블을 다시 그리는 함수
    async function fetchProducts() {
        addLog('상품 목록 로딩 중...');
        try {
            const response = await fetch(`${API_BASE_URL}/products`);
            if (!response.ok) throw new Error(`서버 응답 오류: ${response.status}`);
            const products = await response.json();

            productListBody.innerHTML = '';
            products.forEach(product => {
                const tr = document.createElement('tr');
                tr.innerHTML = `
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.price}</td>
                    <td><strong>${product.stockQuantity}</strong></td>
                    <td>
                        <button class="order-btn" data-id="${product.id}">1개 주문</button>
                        <button class="cart-btn" data-id="${product.id}">장바구니 담기</button>
                        <button class="edit-btn" data-id="${product.id}">수정</button>
                        <button class="delete-btn" data-id="${product.id}">삭제</button>
                    </td>
                `;
                productListBody.appendChild(tr);
            });
            addLog('상품 목록 로딩 완료.');
        } catch (error) {
            addLog(`상품 목록 로딩 실패: ${error.message}`, true);
        }
    }

    // 폼을 초기 상태로 리셋하는 함수
    function resetForm() {
        productForm.reset();
        productIdInput.value = '';
        cancelBtn.style.display = 'none';
    }

    // --- 이벤트 리스너 설정 ---

    // 1. 상품 등록 / 수정 폼 제출 이벤트
    productForm.addEventListener('submit', async (e) => {
        e.preventDefault();
        const id = productIdInput.value;
        const productData = {
            name: nameInput.value,
            price: parseInt(priceInput.value),
            stockQuantity: parseInt(stockQuantityInput.value)
        };
        const method = id ? 'PUT' : 'POST';
        const url = id ? `${API_BASE_URL}/products/${id}` : `${API_BASE_URL}/products`;
        const actionText = id ? '수정' : '등록';

        addLog(`상품 ${actionText} 요청...`);
        try {
            const response = await fetch(url, {
                method: method,
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(productData)
            });
            const responseBody = await response.text();
            if (response.ok) {
                addLog(responseBody);
                resetForm();
                await fetchProducts();
            } else {
                addLog(`${actionText} 실패: ${response.status} - ${responseBody}`, true);
            }
        } catch (error) {
            addLog(`네트워크 오류: ${error.message}`, true);
        }
    });

    // 2. 상품 목록 테이블 안의 버튼들에 대한 이벤트 (이벤트 위임)
    productListBody.addEventListener('click', async (e) => {
        const target = e.target;
        if (target.tagName !== 'BUTTON') return;

        const id = target.dataset.id;
        const TEST_USER_ID = 99; // 테스트용 사용자 ID

        // 주문 버튼
        if (target.classList.contains('order-btn')) {
            addLog(`${id}번 상품 1개 주문 요청...`);
            try {
                const response = await fetch(`${API_BASE_URL}/orders`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ userId: TEST_USER_ID, productId: id, quantity: 1 })
                });
                const responseBody = await response.text();
                if (!response.ok) {
                    addLog(`주문 실패: ${response.status} - ${responseBody}`, true);
                } else {
                    addLog(responseBody);
                    await fetchProducts();
                }
            } catch (error) {
                addLog(`네트워크 오류: ${error.message}`, true);
            }
        }

        // --- '장바구니 담기' 버튼 로직 추가 ---
        if (target.classList.contains('cart-btn')) {
            addLog(`${id}번 상품 장바구니 추가 요청...`);
            try {
                const response = await fetch(`${API_BASE_URL}/carts/users/${TEST_USER_ID}`, {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({ productId: id, quantity: 1 }) // 1개씩 추가
                });
                const responseBody = await response.text();
                if (!response.ok) {
                    addLog(`장바구니 추가 실패: ${response.status} - ${responseBody}`, true);
                } else {
                    addLog(responseBody);
                    // 장바구니에 담아도 재고는 변하지 않으므로, 목록을 새로고침할 필요는 없습니다.
                }
            } catch (error) {
                addLog(`네트워크 오류: ${error.message}`, true);
            }
        }

        // 수정 버튼
        if (target.classList.contains('edit-btn')) {
            const row = target.closest('tr');
            productIdInput.value = id;
            nameInput.value = row.cells[1].textContent;
            priceInput.value = row.cells[2].textContent;
            stockQuantityInput.value = row.cells[3].querySelector('strong').textContent;
            cancelBtn.style.display = 'inline-block';
            addLog(`${id}번 상품 정보를 수정 폼으로 불러왔습니다.`);
        }

        // 삭제 버튼
        if (target.classList.contains('delete-btn')) {
            if (confirm(`정말로 ID ${id} 상품을 삭제하시겠습니까?`)) {
                addLog(`${id}번 상품 삭제 요청...`);
                try {
                    const response = await fetch(`${API_BASE_URL}/products/${id}`, { method: 'DELETE' });
                    const responseBody = await response.text();
                    if (response.ok) {
                        addLog(responseBody);
                        await fetchProducts();
                    } else {
                        addLog(`삭제 실패: ${response.status} - ${responseBody}`, true);
                    }
                } catch (error) {
                    addLog(`네트워크 오류: ${error.message}`, true);
                }
            }
        }
    });

    // 3. 취소 버튼 클릭 이벤트
    cancelBtn.addEventListener('click', resetForm);

    // --- 최초 실행 ---
    fetchProducts();
});