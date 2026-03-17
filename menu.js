// ---------------------------- Hamburger menu ----------------------------
document.addEventListener("DOMContentLoaded", () => { //czeka az cala strona sie wczyta

    const icon = document.querySelector(".menu-icon"); // pobiera ikone menu
    const menu = document.querySelector(".menu"); //pobiera ukryte menu
    let isOpen = false; // zmienna ktora zapamietuje czy menu jest otwarte, false - schowana
    icon.addEventListener("click", () => {
        isOpen = !isOpen; // przelacza wartosc na przeciwna
        menu.style.display = isOpen ? "flex" : "none"; // jezeli isOpen jest true to pokazuje menu, jezeli false to je chowa
    });

    document.addEventListener("click", (e) => { //klikniecie poza menu automatycznie go zamyka
        const clickedInsideMenu = menu.contains(e.target);
        const clickedIcon = icon.contains(e.target);

        if (!clickedInsideMenu && !clickedIcon) {
            menu.style.display = "none";
            isOpen = false;
        }
    });

});
