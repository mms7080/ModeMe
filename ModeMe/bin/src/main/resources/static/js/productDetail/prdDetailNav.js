// JavaScript for navigation menu functionality

// Get all navigation links
const navLinks = document.querySelectorAll('.menu a');

// Add click event listener to each link
navLinks.forEach(link => {
    link.addEventListener('click', (event) => {
        // Prevent default anchor behavior
        event.preventDefault();

        // Remove the 'selected' class from all menu items
        document.querySelectorAll('.menu li').forEach(item => {
            item.classList.remove('selected');
        });

        // Add 'selected' class to the clicked link's parent <li>
        link.parentElement.classList.add('selected');

        // Scroll to the target section
        const targetId = link.getAttribute('href').substring(1);
        const targetSection = document.getElementById(targetId);

        if (targetSection) {
            targetSection.scrollIntoView({ behavior: 'smooth' });
        }
    });
});
