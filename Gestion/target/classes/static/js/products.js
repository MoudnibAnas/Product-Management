document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const productId = this.getAttribute("data-id");

            if (confirm(`Are you sure you want to delete product ID ${productId}?`)) {
                fetch(`/api/products/delete/${productId}`, {
                    method: "DELETE"
                })
                .then(response => {
                    if (response.ok) {
                        alert("Product deleted successfully!");
                        location.reload();
                    } else {
                        alert("Failed to delete product.");
                    }
                })
                .catch(error => {
                    console.error("Error:", error);
                    alert("An error occurred.");
                });
            }
        });
    });
});
document.addEventListener('DOMContentLoaded', function () {
    const editButtons = document.querySelectorAll('.edit-btn');
    const editForm = document.getElementById('editForm');
    const closeButton = document.getElementById('closeButton');
    editButtons.forEach(button => {
        button.addEventListener('click', function () {
            const productId = button.getAttribute('data-id');
            const name = button.getAttribute('data-name');
            const description = button.getAttribute('data-description');
            const price = button.getAttribute('data-price');
            const category = button.getAttribute('data-category');
            const imageUrl = button.getAttribute('data-imageUrl');

            document.getElementById('editProductId').value = productId;
            document.getElementById('editName').value = name;
            document.getElementById('editDescription').value = description;
            document.getElementById('editPrice').value = price;
            document.getElementById('editCategory').value = category;
            document.getElementById('editImageUrl').value = imageUrl;

            editForm.style.display = 'block';
        });
    });
    closeButton.addEventListener('click', function () {
        editForm.style.display = 'none';
    });
});
