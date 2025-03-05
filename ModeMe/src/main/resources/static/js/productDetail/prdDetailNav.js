const navLinks = document.querySelectorAll('.menu a');
navLinks.forEach(link => {
    link.addEventListener('click', (event) => {
        document.querySelectorAll('.menu li').forEach(item => {
            item.classList.remove('selected');
        });

        link.parentElement.classList.add('selected');

        const targetId = link.getAttribute('href').substring(1);
        const targetSection = document.getElementById(targetId);

        if (targetSection) {
            targetSection.scrollIntoView({ behavior: 'smooth' });
        }
    });
});
