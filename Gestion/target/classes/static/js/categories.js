document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const categoryId = this.getAttribute("data-id");
             console.log(categoryId)
            if (confirm(`Are you sure you want to delete category ID ${categoryId}?`)) {
                fetch(`/api/categories/delete/${categoryId}`, {
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
document.querySelectorAll('.edit-btn').forEach(button => {
    button.addEventListener('click', function() {
        const categoryId = this.getAttribute('data-id');
        const categoryName = this.getAttribute('data-name');
        const categoryDescription = this.getAttribute('data-description');

        // Set the form fields to the current category values
        document.getElementById('updateCategoryId').value = categoryId;
        document.getElementById('updateName').value = categoryName;
        document.getElementById('updateDescription').value = categoryDescription;

        // Show the update form
        document.getElementById('hiddenUpdateForm').style.display = 'block';
    });
});

// Close the update form when clicking the close button
document.getElementById('closeUpdateButton').addEventListener('click', function() {
    document.getElementById('hiddenUpdateForm').style.display = 'none';
});
