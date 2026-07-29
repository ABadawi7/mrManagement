// Stores the ID of the employee currently being edited.
// Null means that a new employee will be created.
let editingEmployeeId = null;


// Adds one safe text cell to a table row.
function addCell(row, value) {

    // Creates a new table cell.
    const cell = document.createElement("td");

    // Inserts the value as plain text without interpreting HTML.
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

        // Stops when the backend returns an error.
        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        // Converts the JSON response into a JavaScript array.
        const employees = await response.json();

        // Removes old table rows.
        tableBody.textContent = "";

        // Creates one table row for each employee.
        employees.forEach(employee => {

            // Creates a new table row.
            const row = document.createElement("tr");

            // Adds the employee data safely as text.
            addCell(row, employee.id);
            addCell(row, employee.mitarbeiterNr);
            addCell(row, employee.vorname);
            addCell(row, employee.nachname);
            addCell(row, employee.filialeCode);
            addCell(row, employee.rolle);
            addCell(row, employee.aktiv ? "Ja" : "Nein");
            addCell(row, employee.eintrittsdatum);

            // Creates the table cell for action buttons.
            const actionCell = document.createElement("td");

            // Creates the edit button.
            const editButton = document.createElement("button");

            // Sets the visible button text.
            editButton.textContent = "Bearbeiten";

            // Prevents the button from submitting the form.
            editButton.type = "button";

            // Loads the selected employee into the form.
            editButton.addEventListener("click", () => {
                fillEmployeeForm(employee);
            });

            // Adds the edit button to the action cell.
            actionCell.appendChild(editButton);

            // Adds the action cell to the table row.
            row.appendChild(actionCell);

            // Adds the completed row to the table.
            tableBody.appendChild(row);
        });

    } catch (error) {

        // Removes old table content.
        tableBody.textContent = "";

        // Creates a row for the error message.
        const errorRow = document.createElement("tr");

        // Creates the error message cell.
        const errorCell = document.createElement("td");

        // The error cell covers all nine table columns.
        errorCell.colSpan = 9;

        // Displays a safe error message.
        errorCell.textContent =
            "Mitarbeiter konnten nicht geladen werden.";

        // Adds the error cell to the row.
        errorRow.appendChild(errorCell);

        // Adds the error row to the table.
        tableBody.appendChild(errorRow);

        // Writes the technical error into the browser console.
        console.error("Failed to load employees:", error);
    }
}


// Fills the form with the selected employee data.
function fillEmployeeForm(employee) {

    // Stores the employee ID for the later PUT request.
    editingEmployeeId = employee.id;

    // Copies the employee data into the form fields.
    document.getElementById("vorname").value =
        employee.vorname ?? "";

    document.getElementById("nachname").value =
        employee.nachname ?? "";

    // The HTML ID remains filialCode.
    document.getElementById("filialCode").value =
        employee.filialeCode ?? "";

    document.getElementById("rolle").value =
        employee.rolle ?? "";

    document.getElementById("aktiv").checked =
        employee.aktiv;

    document.getElementById("eintrittsdatum").value =
        employee.eintrittsdatum ?? "";

    // Changes the submit button text while editing.
    document.querySelector(
        "#mitarbeiterFormular button[type='submit']"
    ).textContent = "Änderungen speichern";

    // Removes an old message.
    document.getElementById("meldung").textContent = "";
}


// Sends the form data to the backend.
async function saveEmployee(event) {

    // Prevents the browser from reloading the page.
    event.preventDefault();

    // Finds the message element.
    const messageElement = document.getElementById("meldung");

    // Removes an old message.
    messageElement.textContent = "";

    // Reads the employee data from the form.
    const employee = {
        vorname:
            document.getElementById("vorname").value.trim(),

        nachname:
            document.getElementById("nachname").value.trim(),

        // The property name must match the Java class.
        filialeCode:
        document.getElementById("filialCode").value,

        rolle:
        document.getElementById("rolle").value,

        aktiv:
        document.getElementById("aktiv").checked,

        eintrittsdatum:
            document.getElementById("eintrittsdatum").value || null
    };

    // Uses POST when creating and PUT when editing.
    const method =
        editingEmployeeId === null ? "POST" : "PUT";

    // Selects the correct backend URL.
    const url =
        editingEmployeeId === null
            ? "/api/mitarbeiter"
            : `/api/mitarbeiter/${editingEmployeeId}`;

    try {

        // Sends the employee data to the backend.
        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employee)
        });

        if (response.ok) {

            // Reads and displays the backend response.
            const message = await response.text();
            messageElement.textContent = message;

            // Clears the form.
            document
                .getElementById("mitarbeiterFormular")
                .reset();

            // Activates the checkbox again after resetting.
            document.getElementById("aktiv").checked = true;

            // Ends the editing mode.
            editingEmployeeId = null;

            // Restores the original submit button text.
            document.querySelector(
                "#mitarbeiterFormular button[type='submit']"
            ).textContent = "Speichern";

            // Reloads the current employee list.
            await loadEmployees();

        } else if (response.status === 400) {

            // Displays a validation error.
            messageElement.textContent =
                "Bitte prüfen Sie die eingegebenen Mitarbeiterdaten.";

        } else {

            // Displays a general backend error.
            messageElement.textContent =
                "Mitarbeiter konnte nicht gespeichert werden.";
        }

    } catch (error) {

        // Displays an error when the backend cannot be reached.
        messageElement.textContent =
            "Verbindung zum Server fehlgeschlagen.";

        // Writes the technical error into the browser console.
        console.error("Failed to save employee:", error);
    }
}


// Runs after the HTML document is completely loaded.
document.addEventListener("DOMContentLoaded", () => {

    // Loads the employee list.
    loadEmployees();

    // Connects the form with the save function.
    document
        .getElementById("mitarbeiterFormular")
        .addEventListener("submit", saveEmployee);
});