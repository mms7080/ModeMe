document.addEventListener("DOMContentLoaded", function() {
       const deleteButtons = document.querySelectorAll(".deleteButton");

       deleteButtons.forEach(button => {
           button.addEventListener("click", function() {
               const reviewId = button.getAttribute("data-id");

               if (confirm("리뷰를 삭제하시겠습니까?")) { // 삭제 확인
                   fetch(`/manager/deleteReview/${reviewId}`, {
                       method: 'DELETE',
                       headers: {
                           'Content-Type': 'application/json',
                       },
                   })
                   .then(response => {
                       if (response.ok) {
                           alert('리뷰가 삭제되었습니다.');
                           location.reload(); // 페이지 새로고침
                       } else {
                           alert('리뷰 삭제에 실패했습니다.');
                       }
                   })
                   .catch(error => {
                       console.error('에러 발생:', error);
                       alert('에러가 발생했습니다.');
                   });
               }
           });
       });
   });
