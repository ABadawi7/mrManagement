// Stores the ID of the employee currently being edited.
let editingEmployeeId = null;
// Stores all loaded employees for local filtering
let allEmployees = [];


// Adds one safe text cell to a table row.
function addCell(row, value) {

    // Creates a new table cell.
    const cell = document.createElement("td");

    // Inserts the value as plain text.
    cell.textContent = value ?? "";

    // Adds the cell to the table row.
    row.appendChild(cell);
}


// Loads all employees from the backend.
async function loadEmployees() {

    const tableBody =
        document.getElementById("mitarbeiterTabelle");

    try {

        // Sends a GET request to the backend.
        const response =
            await fetch("/api/mitarbeiter");

        if (!response.ok) {
            throw new Error(`HTTP error: ${response.status}`);
        }

        // Converts the response into a JavaScript array.
        const employees = await response.json();
        //Stores all employees for the search function.
        allEmployees = employees;

        // Displays all loaded employees.
        displayEmployees(employees);

    } catch (error) {

        tableBody.textContent = "";

        const errorRow =
            document.createElement("tr");

        const errorCell =
            document.createElement("td");

        errorCell.colSpan = 9;
        errorCell.textContent =
            "Mitarbeiter konnten nicht geladen werden.";

        errorRow.appendChild(errorCell);
        tableBody.appendChild(errorRow);

        console.error(
            "Failed to load employees:",
            error
        );
    }
}


// Fills the form with the selected employee data.
function fillEmployeeForm(employee) {

    // Stores the selected database ID.
    editingEmployeeId = employee.id;

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

    // Changes the submit button text.
    document.querySelector(
        "#mitarbeiterFormular button[type='submit']"
    ).textContent = "Änderungen speichern";

    document.getElementById("meldung").textContent = "";
}


// Sends the form data to the backend.
async function saveEmployee(event) {

    // Prevents the browser from reloading the page.
    event.preventDefault();

    const messageElement =
        document.getElementById("meldung");

    messageElement.textContent = "";

    // Reads the current form data.
    const employee = {
        vorname:
            document.getElementById("vorname")
                .value
                .trim(),

        nachname:
            document.getElementById("nachname")
                .value
                .trim(),

        filialeCode:
        document.getElementById("filialCode")
            .value,

        rolle:
        document.getElementById("rolle")
            .value,

        aktiv:
        document.getElementById("aktiv")
            .checked,

        eintrittsdatum:
            document.getElementById("eintrittsdatum")
                .value || null
    };

    // Uses POST for new employees and PUT for editing.
    const method =
        editingEmployeeId === null
            ? "POST"
            : "PUT";

    const url =
        editingEmployeeId === null
            ? "/api/mitarbeiter"
            : `/api/mitarbeiter/${editingEmployeeId}`;

    try {

        const response = await fetch(url, {
            method: method,
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(employee)
        });

        if (response.ok) {

            messageElement.textContent =
                await response.text();

            // Clears the form.
            document
                .getElementById("mitarbeiterFormular")
                .reset();

            document.getElementById("aktiv").checked =
                true;

            // Ends the editing mode.
            editingEmployeeId = null;

            document.querySelector(
                "#mitarbeiterFormular button[type='submit']"
            ).textContent = "Speichern";

            // Reloads the table.
            await loadEmployees();

        } else if (response.status === 400) {

            messageElement.textContent =
                "Bitte prüfen Sie die eingegebenen Daten.";

        } else if (response.status === 404) {

            messageElement.textContent =
                "Mitarbeiter wurde nicht gefunden.";

        } else {

            messageElement.textContent =
                "Mitarbeiter konnte nicht gespeichert werden.";
        }

    } catch (error) {

        messageElement.textContent =
            "Verbindung zum Server fehlgeschlagen.";

        console.error(
            "Failed to save employee:",
            error
        );
    }
}


// Deletes an employee by employee number.
async function deleteEmployee(employeeNumber) {

    // Asks the user for confirmation.
    const confirmed = confirm(
        `Mitarbeiter ${employeeNumber} wirklich löschen?`
    );

    if (!confirmed) {
        return;
    }

    const messageElement =
        document.getElementById("meldung");

    try {

        // Sends the DELETE request.
        const response = await fetch(
            `/api/mitarbeiter/nummer/${employeeNumber}`,
            {
                method: "DELETE"
            }
        );

        if (response.status === 404) {

            messageElement.textContent =
                "Mitarbeiter wurde nicht gefunden.";

            return;
        }

        if (!response.ok) {
            throw new Error(
                `HTTP error: ${response.status}`
            );
        }

        messageElement.textContent =
            await response.text();

        // Reloads the employee table.
        await loadEmployees();

    } catch (error) {

        messageElement.textContent =
            "Mitarbeiter konnte nicht gelöscht werden.";

        console.error(
            "Failed to delete employee:",
            error
        );
    }
}
// Displays the given employees in the table.
function displayEmployees(employees) {

    const tableBody =
        document.getElementById("mitarbeiterTabelle");

    // Removes old table rows.
    tableBody.textContent = "";

    employees.forEach(employee => {

        const row = document.createElement("tr");

        addCell(row, employee.id);
        addCell(row, employee.mitarbeiterNr);
        addCell(row, employee.vorname);
        addCell(row, employee.nachname);
        addCell(row, employee.filialeCode);
        addCell(row, employee.rolle);
        addCell(row, employee.aktiv ? "Ja" : "Nein");
        addCell(row, employee.eintrittsdatum);

        const actionCell =
            document.createElement("td");

        const editButton =
            document.createElement("button");

        editButton.type = "button";
        editButton.textContent = "Bearbeiten";

        editButton.addEventListener("click", () => {
            fillEmployeeForm(employee);
        });

        actionCell.appendChild(editButton);

        const deleteButton =
            document.createElement("button");

        deleteButton.type = "button";
        deleteButton.textContent = "Löschen";

        deleteButton.addEventListener("click", async () => {
            await deleteEmployee(employee.mitarbeiterNr);
        });

        actionCell.appendChild(deleteButton);
        row.appendChild(actionCell);
        tableBody.appendChild(row);
    });
}
// Filters employees by search term and branch.
function filterEmployees() {

    const searchTerm =
        document.getElementById("suche")
            .value
            .trim()
            .toLowerCase();

    const selectedBranch =
        document.getElementById("filialFilter").value;

    const filteredEmployees = allEmployees.filter(employee => {

        const employeeNumber =
            String(employee.mitarbeiterNr ?? "")
                .toLowerCase();

        const firstName =
            String(employee.vorname ?? "")
                .toLowerCase();

        const lastName =
            String(employee.nachname ?? "")
                .toLowerCase();

        const matchesSearch =
            employeeNumber.includes(searchTerm)
            || firstName.includes(searchTerm)
            || lastName.includes(searchTerm);

        const matchesBranch =
            selectedBranch === ""
            || employee.filialeCode === selectedBranch;

        return matchesSearch && matchesBranch;
    });

    displayEmployees(filteredEmployees);
}


// Runs after the HTML document is loaded.
document.addEventListener("DOMContentLoaded", () => {

    // Loads all employees.
    loadEmployees();

    // Connects the form with the save function.
    document.getElementById("mitarbeiterFormular").addEventListener("submit", saveEmployee);
    // Connects the search field with the filter function.
    document.getElementById("suche").addEventListener("input",filterEmployees);
    // Connects the branch filter with the filter function
    document.getElementById("filialFilter").addEventListener("change",filterEmployees);
});