// ---------------------------- Wzorzec imienia ----------------------------
const namePattern = /^[A-Z][a-z]{1,}$/;
const cityPattern = /^[A-ZĄĆĘŁŃÓŚŹŻ][a-ząćęłńóśźż]+$/;


// ---------------------------- Pobranie elementów ----------------------------
const form = document.getElementById("santa-form");
const nameInput = document.getElementById("name");
const ageInput = document.getElementById("age");
const nameError = document.getElementById("name-error");
const ageError = document.getElementById("age-error");
const letterSection = document.getElementById("santa-letter");
const letterText = document.getElementById("letter-text");
const tabButtons = document.querySelectorAll(".tab-button");
const aboutSection = document.getElementById("about-section");
const wishlistSection = document.getElementById("wishlist-section");
const cityInput = document.getElementById("city");
const cityError = document.getElementById("city-error");


// ---------------------------- Przelaczanie sekcji  About Me i My Wishlist ----------------------------
tabButtons.forEach(button => {
    button.addEventListener("click", () => {

        tabButtons.forEach(btn => btn.classList.remove("active"));
        button.classList.add("active");

        if (button.dataset.section === "about") {
            aboutSection.classList.remove("hidden");
            wishlistSection.classList.add("hidden");
        }

        if (button.dataset.section === "wishlist") {
            wishlistSection.classList.remove("hidden");
            aboutSection.classList.add("hidden");

        
            nameError.textContent = "";
            ageError.textContent = "";
            cityError.textContent = "";
        }
    });
});

// ---------------------------- Walidacja imienia ----------------------------
function validateName() {
    const value = nameInput.value.trim();
    const label = document.querySelector("label[for='name']");

    if (value === "") {
        nameError.textContent = "Name is required.";
        nameError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    if (!namePattern.test(value)) {
        nameError.textContent = "Start with a capital letter. Only letters allowed.";
        nameError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    nameError.textContent = "";
    nameError.classList.remove("error");
    label.classList.remove("error");
    return true;
}


// ---------------------------- Walidacja wieku ----------------------------
function validateAge() {
    const value = ageInput.value.trim();
    const label = document.querySelector("label[for='age']");
    const ageNumber = Number(value);

    if (value === "") {
        ageError.textContent = "Age is required.";
        ageError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    if (isNaN(ageNumber) || ageNumber < 1 || ageNumber > 18) {
        ageError.textContent = "Please enter a valid age (1–18).";
        ageError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    ageError.textContent = "";
    ageError.classList.remove("error");
    label.classList.remove("error");
    return true;
}


function validateCity() {
    const value = cityInput.value.trim();
    const label = document.querySelector("label[for='city']");


    if (value === "") {
        cityError.textContent = "City or magical land is required.";
        cityError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    if (!cityPattern.test(value)) {
        cityError.textContent = "Start with a capital letter. Only letters allowed.";
        cityError.classList.add("error");
        label.classList.add("error");
        return false;
    }

    
    cityError.textContent = "";
    cityError.classList.remove("error");
    label.classList.remove("error");
    return true;
}


// ---------------------------- Walidacja na żywo ----------------------------
nameInput.addEventListener("input", validateName); // na żywo
ageInput.addEventListener("input", validateAge);
cityInput.addEventListener("input", validateCity);
cityInput.addEventListener("blur", validateCity);
nameInput.addEventListener("blur", validateName); 
ageInput.addEventListener("blur", validateAge);

// ---------------------------- Send to Santa, Przesylanie formularza ----------------------------
form.addEventListener("submit", (e) => {
    e.preventDefault();

    const nameValid = validateName();
    const ageValid = validateAge();
    const cityValid = validateCity();

    if (!nameValid || !ageValid || !cityValid) return;

   
    const nameValue = nameInput.value.trim();
    const ageValue = ageInput.value.trim();
    const cityValue = cityInput.value.trim();

    const giftSelected = document.querySelector("input[name='gift']:checked");

    let giftValue = "";
    if (giftSelected) {
        giftValue = giftSelected.value;
    }

    nameError.textContent = "";
    ageError.textContent = "";
    cityError.textContent = "";

    tabButtons.forEach(btn => btn.classList.remove("active"));
    tabButtons[0].classList.add("active");
    wishlistSection.classList.add("hidden");
    aboutSection.classList.remove("hidden");

    document.querySelector("#santa-letter h2").textContent = "Your letter summary";

    letterText.textContent = 
        `Dear Santa, I'm ${nameValue}, ${ageValue} years old. ` +
        `I live in ${cityValue}. ` +
        (giftValue ? `This year I would like to receive a ${giftValue}.` : "");

    letterSection.classList.remove("hidden");
});



// ---------------------------- Start Over ----------------------------
form.addEventListener("reset", () => {
    setTimeout(() => { // czysci wszystkie pola input

        nameError.textContent = ""; // usuwa komunikaty bledow
        ageError.textContent = "";

        // przywraca zakładkę About me
        tabButtons.forEach(btn => btn.classList.remove("active"));
        tabButtons[0].classList.add("active");
        wishlistSection.classList.add("hidden");
        aboutSection.classList.remove("hidden");

        // ukrywa list
        letterSection.classList.add("hidden");
        letterText.textContent = "";
    }, 0);
});
