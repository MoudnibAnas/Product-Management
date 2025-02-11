document.addEventListener("DOMContentLoaded", function () {
    document.querySelectorAll(".delete-btn").forEach(button => {
        button.addEventListener("click", function () {
            const reviewText = this.getAttribute("data-id");
            console.log("Sending " + reviewText)
            if (confirm(`Are you sure you want to delete the review ?`)) {
                fetch(`/api/reviews/delete/${reviewText}`, {
                    method: "DELETE"
                })
                .then(response => {
                    if (response.ok) {
                        alert("Review deleted successfully!");
                        location.reload();
                    } else {
                        alert("Failed to delete review.");
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
