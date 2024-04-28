function flipCard(element) {
    const cardContent = element.querySelector('.card-content');
    cardContent.style.transform = (cardContent.style.transform === 'rotateY(180deg)') ? 'rotateY(0deg)' : 'rotateY(180deg)';
}
