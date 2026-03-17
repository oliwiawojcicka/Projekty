document.addEventListener("DOMContentLoaded", () => {
    const counter = document.getElementById("days-counter");

    const today = new Date();
    const christmas = new Date("2025-12-24"); 

    today.setHours(0, 0, 0, 0);
    christmas.setHours(0, 0, 0, 0);

    const ms = christmas - today;
    const days = Math.ceil(ms / (1000 * 60 * 60 * 24));

    counter.textContent = days;
});


