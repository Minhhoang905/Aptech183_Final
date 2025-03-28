document.addEventListener("DOMContentLoaded", function () {
    updateCartIcon(); // Cập nhật icon giỏ hàng khi load trang

    // 🛒 Lấy tất cả các nút "Thêm vào giỏ hàng"
    document.querySelectorAll(".add-to-cart").forEach(button => {
        button.addEventListener("click", function () {
            let productName = this.getAttribute("data-name");
            let price = parseFloat(this.getAttribute("data-price"));
            let quantityInputId = this.getAttribute("data-quantity-id");
            let quantityInput = document.getElementById(quantityInputId);
            let quantity = parseInt(quantityInput.value);

            // ⚠️ Kiểm tra số lượng hợp lệ
            if (!quantity || quantity < 1) {
                alert("❌ Số lượng không hợp lệ! Vui lòng nhập số lượng hợp lệ.");
                quantityInput.value = 1; // Reset về 1 nếu nhập sai
                return;
            }

            // 📦 Lấy giỏ hàng từ localStorage
            let cart = JSON.parse(localStorage.getItem("cart")) || [];

            // 🔍 Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            let existingProduct = cart.find(item => item.productName === productName);
            if (existingProduct) {
                existingProduct.quantity += quantity;
            } else {
                cart.push({ productName, price, quantity });
            }

            // 💾 Lưu giỏ hàng vào localStorage
            localStorage.setItem("cart", JSON.stringify(cart));

            // 🔄 Cập nhật icon giỏ hàng
            updateCartIcon();

            // ✅ Hiển thị thông báo thành công
            alert(`✅ Đã thêm ${quantity} x ${productName} vào giỏ hàng!`);
        });
    });

    // 🎯 Hàm cập nhật số lượng hiển thị trên icon giỏ hàng
    function updateCartIcon() {
        let cart = JSON.parse(localStorage.getItem("cart")) || [];
        let cartCount = cart.reduce((acc, item) => acc + item.quantity, 0);

        // Kiểm tra nếu #cart-count tồn tại trước khi cập nhật
        let cartCountElement = document.getElementById("cart-count");
        if (cartCountElement) {
            cartCountElement.textContent = `(${cartCount})`;
        }
    }
});
