// Adds one safe text cell to a table row.
function addCell(row, value) {

    // Creates a new table cell.
    const cell = document.createElement("td");

    // Inserts the value as plain text.
    cell.textContent = value ?? "";

    // Adds the cell to the table row.
    row.appendChild(cell);
}

// Loads all employees from the backend API.
async function loadEmployees() {

    // Finds the table body in the HTML document.
    const tableBody = document.getElementById("mitarbeiterTabelle");

    try {
        // Sends a GET request to the backend.
        const response = await fetch("/api/mitarbeiter");

        // Checks whether the request was successful.
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        // Converts the JSON response into a JavaScript array.
        const employees = await response.json();

        // Removes old rows before displaying current data.
        tableBody.textContent = "";

        // Creates one table row for each employee.
        employees.forEach(employee => {

            // Creates a new table row.
            const row = document.createElement("tr");

            // Adds all employee values safely as text.
            addCell(row, employee.id);
            addCell(row, employee.mitarbeiterNr);
            addCell(row, employee.vorname);
            addCell(row, employee.nachname);
            addCell(row, employee.filialeCode);
            addCell(row, employee.rolle);
            addCell(row, employee.aktiv ? "Ja" : "Nein");
            addCell(row, employee.eintrittsdatum);

            // Adds the completed row to the table.
            tableBody.appendChild(row);
        });

    } catch (error) {
        // Removes old content when loading fails.
        tableBody.textContent = "";

        // Creates an error row.
        const errorRow = document.createElement("tr");

        // Creates an error cell.
        const errorCell = document.createElement("td");

        // The error cell covers all table columns.
        errorCell.colSpan = 8;

        // Displays a safe error message.
        errorCell.textContent = "Mitarbeiter konnten nicht geladen werden.";

        // Adds the error cell to the row.
        errorRow.appendChild(errorCell);

        // Adds the error row to the table.
        tableBody.appendChild(errorRow);

        // Writes the technical error into the browser console.
        console.error("Failed to load employees:", error);
    }
}

// Sends the employee form data to the backend.
async function saveEmployee(event) {

    // Prevents the browser from reloading the page.
    event.preventDefault();

    // Reads the values from the form.
    const employee = {
        vorname: document.getElementById("vorname").value.trim(),
        nachname: document.getElementById("nachname").value.trim(),
        filialeCode: document.getElementById("filialCode").value,
        rolle: document.getElementById("rolle").value,
        aktiv: document.getElementById("aktiv").checked,
        eintrittsdatum:
            document.getElementById("eintrittsdatum").value || null
    };

    // Finds the message element on the page.
    const messageElement = document.getElementById("meldung");

    try {
        // Sends the employee data to the backend.
        const response = await fetch("/api/mitarbeiter", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employee)
        });

        if (response.ok) {

            // Reads and displays the success message.
            const message = await response.text();
            messageElement.textContent = message;

            // Clears the form after successful saving.
            document.getElementById("mitarbeiterFormular").reset();

            // Activates the checkbox again after resetting.
            document.getElementById("aktiv").checked = true;

            // Reloads the employee table.
            await loadEmployees();

        } else if (response.status === 400) {

            // Displays a clear validation message.
            messageElement.textContent =
                "Bitte prüfen Sie die eingegebenen Mitarbeiterdaten.";

        } else {

            // Displays a general error message.
            messageElement.textContent =
                "Mitarbeiter konnte nicht gespeichert werden.";
        }

    } catch (error) {

        // Displays a message when the backend cannot be reached.
        messageElement.textContent =
            "Verbindung zum Server fehlgeschlagen.";

        // Writes the technical error into the browser console.
        console.error("Failed to save employee:", error);
    }
}

// Loads employees after the HTML page is fully loaded.
document.addEventListener("DOMContentLoaded", () => {

    // Loads the current employee list.
    loadEmployees();

    // Connects the form with the save function.
    document
        .getElementById("mitarbeiterFormular")
        .addEventListener("submit", saveEmployee);
});