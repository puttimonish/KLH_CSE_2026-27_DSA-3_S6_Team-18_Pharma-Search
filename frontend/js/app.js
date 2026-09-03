/* =====================================================
   PHARMASEARCH FRONTEND
   ===================================================== */

const API_BASE_URL = "https://miniature-pancake-69q4j9x5465vh4qx-8080.app.github.dev";

// =====================================================
// DOM ELEMENTS
// =====================================================

const searchInput =
    document.getElementById("searchInput");

const searchButton =
    document.getElementById("searchButton");

const clearButton =
    document.getElementById("clearButton");

const resultsContainer =
    document.getElementById("results");

const resultCount =
    document.getElementById("resultCount");

const loading =
    document.getElementById("loading");

const errorMessage =
    document.getElementById("errorMessage");

const medicineCount =
    document.getElementById("medicineCount");

const emptyState =
    document.getElementById("emptyState");

const sortSelect =
    document.getElementById("sortSelect");

const themeButton =
    document.getElementById("themeButton");

const favoritesButton =
    document.getElementById("favoritesButton");

const favoritesCount =
    document.getElementById("favoritesCount");

const recentSection =
    document.getElementById("recentSection");

const recentSearches =
    document.getElementById("recentSearches");

const clearHistoryButton =
    document.getElementById("clearHistoryButton");

const medicineModal =
    document.getElementById("medicineModal");

const modalBody =
    document.getElementById("modalBody");

const modalClose =
    document.getElementById("modalClose");

const modalOverlay =
    document.getElementById("modalOverlay");

const favoritesModal =
    document.getElementById("favoritesModal");

const favoritesList =
    document.getElementById("favoritesList");

const favoritesClose =
    document.getElementById("favoritesClose");

const favoritesOverlay =
    document.getElementById("favoritesOverlay");

const toast =
    document.getElementById("toast");

const toastMessage =
    document.getElementById("toastMessage");

const backToTop =
    document.getElementById("backToTop");


// =====================================================
// APPLICATION STATE
// =====================================================

let currentMedicines = [];

let currentQuery = "";

let favorites =
    JSON.parse(
        localStorage.getItem("pharmaFavorites") || "[]"
    );

let recent =
    JSON.parse(
        localStorage.getItem("pharmaRecent") || "[]"
    );


// =====================================================
// LOAD MEDICINE COUNT
// =====================================================

async function loadMedicineCount() {

    try {

        const response =
            await fetch(`${API_BASE_URL}/api/count`);

        if (!response.ok) {
            throw new Error("Count request failed");
        }

        const count =
            await response.json();

        medicineCount.textContent =
            Number(count).toLocaleString();

    } catch (error) {

        medicineCount.textContent =
            "Unavailable";

        console.error(error);
    }
}


// =====================================================
// SEARCH MEDICINES
// =====================================================

async function searchMedicines() {

    const query =
        searchInput.value.trim();

    if (!query) {

        showToast(
            "Enter a medicine or composition to search."
        );

        searchInput.focus();

        return;
    }


    currentQuery = query;

    loading.classList.remove("hidden");

    errorMessage.classList.add("hidden");

    emptyState.classList.add("hidden");

    resultsContainer.innerHTML = "";

    resultCount.textContent =
        "Searching...";


    searchButton.disabled = true;

    searchButton.textContent =
        "Searching...";


    try {

        const response =
            await fetch(
                `${API_BASE_URL}/api/search?query=${encodeURIComponent(query)}`
            );


        if (!response.ok) {

            throw new Error(
                `Search failed: ${response.status}`
            );
        }


        const medicines =
            await response.json();


        currentMedicines =
            Array.isArray(medicines)
                ? medicines
                : [];


        addRecentSearch(query);

        displayResults(currentMedicines);

    } catch (error) {

        console.error(error);

        errorMessage.textContent =
            "Unable to connect to the PharmaSearch backend. Make sure Spring Boot is running on port 8080.";

        errorMessage.classList.remove("hidden");

        resultCount.textContent =
            "Search failed";

    } finally {

        loading.classList.add("hidden");

        searchButton.disabled = false;

        searchButton.textContent =
            "Search";
    }
}


// =====================================================
// DISPLAY RESULTS
// =====================================================

function displayResults(medicines) {

    resultsContainer.innerHTML = "";

    let sorted =
        [...medicines];


    // -----------------------------
    // SORT
    // -----------------------------

    const sort =
        sortSelect.value;


    if (sort === "price-low") {

        sorted.sort(
            (a, b) =>
                Number(a.price || 0) -
                Number(b.price || 0)
        );

    } else if (sort === "price-high") {

        sorted.sort(
            (a, b) =>
                Number(b.price || 0) -
                Number(a.price || 0)
        );

    } else if (sort === "name") {

        sorted.sort(
            (a, b) =>
                String(a.name || "")
                    .localeCompare(
                        String(b.name || "")
                    )
        );
    }


    resultCount.textContent =
        `${sorted.length} result${sorted.length === 1 ? "" : "s"} found`;


    if (sorted.length === 0) {

        emptyState.classList.remove("hidden");

        return;
    }


    emptyState.classList.add("hidden");


    sorted.forEach(
        (medicine, index) => {

            const card =
                createMedicineCard(
                    medicine,
                    index
                );

            resultsContainer.appendChild(card);
        }
    );
}


// =====================================================
// CREATE MEDICINE CARD
// =====================================================

function createMedicineCard(
    medicine,
    index
) {

    const card =
        document.createElement("article");

    card.className =
        "medicine-card";


    card.style.animationDelay =
        `${Math.min(index * 0.04, 0.5)}s`;


    const isFavorite =
        isMedicineFavorite(medicine);


    const composition =
        getComposition(medicine);


    card.innerHTML = `

        <div class="card-top">

            <h3>
                ${highlight(
                    medicine.name || "Unknown Medicine"
                )}
            </h3>

            <button
                class="favorite-button"
                title="Add to favorites"
                aria-label="Favorite medicine"
            >
                ${isFavorite ? "❤️" : "🤍"}
            </button>

        </div>


        <p>
            <strong>Manufacturer:</strong>
            ${escapeHTML(
                medicine.manufacturer || "N/A"
            )}
        </p>


        <p>
            <strong>Type:</strong>
            ${escapeHTML(
                medicine.type || "N/A"
            )}
        </p>


        <p>
            <strong>Pack:</strong>
            ${escapeHTML(
                medicine.packSize || "N/A"
            )}
        </p>


        <p>
            <strong>Composition:</strong>
            ${highlight(composition || "N/A")}
        </p>


        <div class="card-footer">

            <p class="price">
                ₹${formatPrice(medicine.price)}
            </p>

            <button
                class="view-button"
            >
                View details →
            </button>

        </div>
    `;


    // FAVORITE

    const favoriteButton =
        card.querySelector(
            ".favorite-button"
        );

    favoriteButton.addEventListener(
        "click",
        () => {

            toggleFavorite(medicine);

            favoriteButton.textContent =
                isMedicineFavorite(medicine)
                    ? "❤️"
                    : "🤍";
        }
    );


    // DETAILS

    const viewButton =
        card.querySelector(
            ".view-button"
        );

    viewButton.addEventListener(
        "click",
        () => openMedicineModal(medicine)
    );


    return card;
}


// =====================================================
// MEDICINE DETAILS MODAL
// =====================================================

function openMedicineModal(medicine) {

    const composition =
        getComposition(medicine);


    modalBody.innerHTML = `

        <h2 class="detail-title">
            ${escapeHTML(
                medicine.name || "Medicine"
            )}
        </h2>


        <div class="detail-row">

            <span>Manufacturer</span>

            <span>
                ${escapeHTML(
                    medicine.manufacturer || "N/A"
                )}
            </span>

        </div>


        <div class="detail-row">

            <span>Medicine Type</span>

            <span>
                ${escapeHTML(
                    medicine.type || "N/A"
                )}
            </span>

        </div>


        <div class="detail-row">

            <span>Pack Size</span>

            <span>
                ${escapeHTML(
                    medicine.packSize || "N/A"
                )}
            </span>

        </div>


        <div class="detail-row">

            <span>Composition</span>

            <span>
                ${escapeHTML(
                    composition || "N/A"
                )}
            </span>

        </div>


        <div class="modal-price">
            ₹${formatPrice(medicine.price)}
        </div>


        <br>


        <button
            id="copyMedicineButton"
            class="view-button"
        >
            📋 Copy medicine details
        </button>
    `;


    document
        .getElementById("copyMedicineButton")
        .addEventListener(
            "click",
            () => copyMedicineDetails(medicine)
        );


    medicineModal.classList.remove("hidden");

    document.body.style.overflow =
        "hidden";
}


// =====================================================
// CLOSE MEDICINE MODAL
// =====================================================

function closeMedicineModal() {

    medicineModal.classList.add("hidden");

    document.body.style.overflow =
        "";
}


// =====================================================
// COPY MEDICINE DETAILS
// =====================================================

async function copyMedicineDetails(
    medicine
) {

    const text = `

Medicine: ${medicine.name || "N/A"}

Manufacturer: ${medicine.manufacturer || "N/A"}

Type: ${medicine.type || "N/A"}

Pack Size: ${medicine.packSize || "N/A"}

Composition: ${getComposition(medicine)}

Price: ₹${formatPrice(medicine.price)}

    `.trim();


    try {

        await navigator.clipboard.writeText(text);

        showToast(
            "Medicine details copied!"
        );

    } catch {

        showToast(
            "Unable to copy details."
        );
    }
}


// =====================================================
// FAVORITES
// =====================================================

function getMedicineId(medicine) {

    return String(
        medicine.id ??
        medicine.name
    );
}


function isMedicineFavorite(medicine) {

    return favorites.some(
        item =>
            getMedicineId(item) ===
            getMedicineId(medicine)
    );
}


function toggleFavorite(medicine) {

    const id =
        getMedicineId(medicine);


    const existingIndex =
        favorites.findIndex(
            item =>
                getMedicineId(item) === id
        );


    if (existingIndex >= 0) {

        favorites.splice(
            existingIndex,
            1
        );

        showToast(
            "Removed from favorites"
        );

    } else {

        favorites.push(medicine);

        showToast(
            "❤️ Added to favorites"
        );
    }


    localStorage.setItem(
        "pharmaFavorites",
        JSON.stringify(favorites)
    );


    updateFavoritesCount();
}


function updateFavoritesCount() {

    favoritesCount.textContent =
        favorites.length;
}


function openFavorites() {

    renderFavorites();

    favoritesModal.classList.remove(
        "hidden"
    );

    document.body.style.overflow =
        "hidden";
}


function closeFavorites() {

    favoritesModal.classList.add(
        "hidden"
    );

    document.body.style.overflow =
        "";
}


function renderFavorites() {

    if (favorites.length === 0) {

        favoritesList.innerHTML = `

            <div class="empty-state">

                <div class="empty-icon">
                    🤍
                </div>

                <h3>
                    No favorites yet
                </h3>

                <p>
                    Click the heart icon on a medicine
                    to save it here.
                </p>

            </div>
        `;

        return;
    }


    favoritesList.innerHTML =
        "";


    favorites.forEach(
        medicine => {

            const item =
                document.createElement("div");

            item.className =
                "medicine-card";


            item.innerHTML = `

                <div class="card-top">

                    <h3>
                        ${escapeHTML(
                            medicine.name || "Medicine"
                        )}
                    </h3>

                    <button
                        class="favorite-button"
                    >
                        ❤️
                    </button>

                </div>


                <p>
                    <strong>
                        Manufacturer:
                    </strong>

                    ${escapeHTML(
                        medicine.manufacturer || "N/A"
                    )}
                </p>


                <p>
                    <strong>
                        Composition:
                    </strong>

                    ${escapeHTML(
                        getComposition(medicine)
                    )}
                </p>


                <div class="card-footer">

                    <p class="price">
                        ₹${formatPrice(medicine.price)}
                    </p>

                    <button
                        class="view-button"
                    >
                        View details →
                    </button>

                </div>
            `;


            item
                .querySelector(".favorite-button")
                .addEventListener(
                    "click",
                    () => {

                        toggleFavorite(medicine);

                        renderFavorites();
                    }
                );


            item
                .querySelector(".view-button")
                .addEventListener(
                    "click",
                    () => {

                        closeFavorites();

                        openMedicineModal(
                            medicine
                        );
                    }
                );


            favoritesList.appendChild(
                item
            );
        }
    );
}


// =====================================================
// RECENT SEARCHES
// =====================================================

function addRecentSearch(query) {

    const clean =
        query.trim();


    recent =
        recent.filter(
            item =>
                item.toLowerCase() !==
                clean.toLowerCase()
        );


    recent.unshift(clean);


    recent =
        recent.slice(0, 8);


    localStorage.setItem(
        "pharmaRecent",
        JSON.stringify(recent)
    );


    renderRecentSearches();
}


function renderRecentSearches() {

    if (recent.length === 0) {

        recentSection.classList.add(
            "hidden"
        );

        return;
    }


    recentSection.classList.remove(
        "hidden"
    );


    recentSearches.innerHTML =
        "";


    recent.forEach(
        query => {

            const button =
                document.createElement("button");

            button.className =
                "recent-item";


            button.innerHTML =
                `🕘 ${escapeHTML(query)}`;


            button.addEventListener(
                "click",
                () => {

                    searchInput.value =
                        query;

                    updateClearButton();

                    searchMedicines();

                    window.scrollTo({
                        top: 0,
                        behavior: "smooth"
                    });
                }
            );


            recentSearches.appendChild(
                button
            );
        }
    );
}


function clearRecentSearches() {

    recent = [];

    localStorage.removeItem(
        "pharmaRecent"
    );

    renderRecentSearches();

    showToast(
        "Search history cleared"
    );
}


// =====================================================
// DARK MODE
// =====================================================

function loadTheme() {

    const theme =
        localStorage.getItem(
            "pharmaTheme"
        );


    if (theme === "dark") {

        document.body.classList.add(
            "dark"
        );

        themeButton.textContent =
            "☀️";

    } else {

        themeButton.textContent =
            "🌙";
    }
}


function toggleTheme() {

    document.body.classList.toggle(
        "dark"
    );


    const dark =
        document.body.classList.contains(
            "dark"
        );


    localStorage.setItem(
        "pharmaTheme",
        dark
            ? "dark"
            : "light"
    );


    themeButton.textContent =
        dark
            ? "☀️"
            : "🌙";
}


// =====================================================
// HIGHLIGHT SEARCH TERM
// =====================================================

function highlight(value) {

    const escaped =
        escapeHTML(value);


    if (!currentQuery) {
        return escaped;
    }


    const words =
        currentQuery
            .split(/\s+/)
            .filter(Boolean)
            .map(word =>
                word.replace(
                    /[.*+?^${}()|[\]\\]/g,
                    "\\$&"
                )
            );


    if (words.length === 0) {
        return escaped;
    }


    const regex =
        new RegExp(
            `(${words.join("|")})`,
            "gi"
        );


    return escaped.replace(
        regex,
        "<mark>$1</mark>"
    );
}


// =====================================================
// UTILITIES
// =====================================================

function getComposition(medicine) {

    const first =
        medicine.composition1 || "";

    const second =
        medicine.composition2 || "";


    if (first && second) {

        return `${first} + ${second}`;
    }


    return first || second;
}


function formatPrice(price) {

    const number =
        Number(price);


    if (!Number.isFinite(number)) {
        return "0.00";
    }


    return number.toFixed(2);
}


function escapeHTML(value) {

    if (
        value === null ||
        value === undefined
    ) {
        return "";
    }


    return String(value)

        .replace(
            /&/g,
            "&amp;"
        )

        .replace(
            /</g,
            "&lt;"
        )

        .replace(
            />/g,
            "&gt;"
        )

        .replace(
            /"/g,
            "&quot;"
        )

        .replace(
            /'/g,
            "&#039;"
        );
}


// =====================================================
// TOAST
// =====================================================

let toastTimer;


function showToast(message) {

    toastMessage.textContent =
        message;


    toast.classList.add(
        "show"
    );


    clearTimeout(
        toastTimer
    );


    toastTimer =
        setTimeout(
            () => {

                toast.classList.remove(
                    "show"
                );

            },
            2500
        );
}


// =====================================================
// CLEAR BUTTON
// =====================================================

function updateClearButton() {

    if (
        searchInput.value.trim()
    ) {

        clearButton.classList.remove(
            "hidden"
        );

    } else {

        clearButton.classList.add(
            "hidden"
        );
    }
}


function clearSearch() {

    searchInput.value =
        "";

    currentQuery =
        "";

    resultsContainer.innerHTML =
        "";

    resultCount.textContent =
        "Search for a medicine to begin";

    emptyState.classList.add(
        "hidden"
    );

    errorMessage.classList.add(
        "hidden"
    );

    updateClearButton();

    searchInput.focus();
}


// =====================================================
// EVENT LISTENERS
// =====================================================

searchButton.addEventListener(
    "click",
    searchMedicines
);


searchInput.addEventListener(
    "keydown",
    event => {

        if (event.key === "Enter") {

            event.preventDefault();

            searchMedicines();
        }


        if (
            event.key === "Escape"
        ) {

            clearSearch();
        }
    }
);


searchInput.addEventListener(
    "input",
    updateClearButton
);


clearButton.addEventListener(
    "click",
    clearSearch
);


sortSelect.addEventListener(
    "change",
    () => {

        if (
            currentMedicines.length
        ) {

            displayResults(
                currentMedicines
            );
        }
    }
);


themeButton.addEventListener(
    "click",
    toggleTheme
);


favoritesButton.addEventListener(
    "click",
    openFavorites
);


favoritesClose.addEventListener(
    "click",
    closeFavorites
);


favoritesOverlay.addEventListener(
    "click",
    closeFavorites
);


modalClose.addEventListener(
    "click",
    closeMedicineModal
);


modalOverlay.addEventListener(
    "click",
    closeMedicineModal
);


clearHistoryButton.addEventListener(
    "click",
    clearRecentSearches
);


// =====================================================
// SEARCH SUGGESTIONS
// =====================================================

document
    .querySelectorAll(".suggestion")
    .forEach(button => {

        button.addEventListener(
            "click",
            () => {

                searchInput.value =
                    button.dataset.query;

                updateClearButton();

                searchMedicines();
            }
        );
    });


// =====================================================
// BACK TO TOP
// =====================================================

window.addEventListener(
    "scroll",
    () => {

        if (window.scrollY > 500) {

            backToTop.classList.remove(
                "hidden"
            );

        } else {

            backToTop.classList.add(
                "hidden"
            );
        }
    }
);


backToTop.addEventListener(
    "click",
    () => {

        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    }
);


// =====================================================
// ESCAPE TO CLOSE MODALS
// =====================================================

document.addEventListener(
    "keydown",
    event => {

        if (event.key !== "Escape") {
            return;
        }


        if (
            !medicineModal.classList.contains(
                "hidden"
            )
        ) {

            closeMedicineModal();
        }


        if (
            !favoritesModal.classList.contains(
                "hidden"
            )
        ) {

            closeFavorites();
        }
    }
);


// =====================================================
// INITIALIZE
// =====================================================

loadTheme();

loadMedicineCount();

updateFavoritesCount();

renderRecentSearches();

updateClearButton();
