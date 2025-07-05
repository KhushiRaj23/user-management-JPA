
// This is base url for backend request 
const API = "http://localhost:8080/api/users";    

//format date and time in pattern like "Jul 05, 2025, 12:30 PM".
const dateFormat = {                 
  year: "numeric",
  month: "short",
  day: "2-digit",
  hour: "2-digit",
  minute: "2-digit"
};

//---REFRENCES FROM CLIENT -----
const userForm     = document.getElementById("userForm");
const userIdField  = document.getElementById("userId");
const nameField    = document.getElementById("name");
const emailField   = document.getElementById("email");
const nameError    = document.getElementById("nameError");
const emailError   = document.getElementById("emailError");
const resetBtn     = document.getElementById("resetFormbtn");
const msgBox       = document.getElementById("messageBox");
const tableBody    = document.getElementById("userTableBody");

//Utility Functions:- 
// Converts an ISO date string (e.g. "2025-07-05T11:30:00Z") to readable format.
const fmtDate = iso =>
  iso ? new Date(iso).toLocaleString(undefined, dateFormat) : "-";


//display success/error msgs and hide after 3.5 sec.
function showMessage(type, text, ms = 3500) {
  msgBox.textContent = text;
  msgBox.className = `message-box ${type}`; // success | error
  msgBox.style.display = "block";
  clearTimeout(msgBox._timer);
  msgBox._timer = setTimeout(() => (msgBox.style.display = "none"), ms);
}

// Resets the form fields and clears any validation errors.
function clearForm() {
  userForm.reset();
  userIdField.value = "";
  [nameError, emailError].forEach(el => (el.textContent = ""));
}

//----rendering table-----
// Returns a <tr> with user data and Edit/Delete buttons.
// data-id stores user ID for future access.
function rowHTML(u) {
  return `
    <tr data-id="${u.id}">
      <td data-label="ID">${u.id}</td>
      <td data-label="Name">${u.name}</td>
      <td data-label="Email">${u.email}</td>
      <td data-label="Created At">${fmtDate(u.createdAt)}</td>
      <td data-label="Updated At">${fmtDate(u.updatedAt)}</td>
      <td data-label="Actions">
        <button class="action-btn edit">Edit</button>
        <button class="action-btn delete">Delete</button>
      </td>
    </tr>`;
}



// Sends a GET request to fetch all users.
// Renders them using rowHTML() or shows fallback messages.
async function refreshUsers() {
  tableBody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:10px;">Loading…</td></tr>`;
  try {
    const list = await fetch(API).then(r => r.json());
    tableBody.innerHTML = list.length
      ? list.map(rowHTML).join("")
      : `<tr><td colspan="6" style="text-align:center;padding:10px;">No users yet</td></tr>`;
  } catch (err) {
    showMessage("error", "Failed to load users 😢");
  }
}

//---add/update------
userForm.addEventListener("submit", async e => {
  // Prevents the form’s default submit behavior.
  e.preventDefault();



  // Simple front-end validation to ensure name and email aren’t empty.
  [nameError, emailError].forEach(el => (el.textContent = ""));
  if (!nameField.value.trim()) { nameError.textContent  = "Name is required";  return; }
  if (!emailField.value.trim()) { emailError.textContent = "Email is required"; return; }


  //Wraps user data in an array to support batch creation later (though only one user is used here).
  const payload = [{
    name:  nameField.value.trim(),
    email: emailField.value.trim()
  }];



// Determines if this is an edit (PUT) or new create (POST).
// Sends data accordingly (single object for PUT, array for POST).
  const id     = userIdField.value;
  const url    = id ? `${API}/${id}` : API;
  const method = id ? "PUT" : "POST";
  const body   = id ? JSON.stringify(payload[0]) : JSON.stringify(payload);

  try {

    //Sends the form data to the backend using Fetch API.
    const res = await fetch(url, {
      method,
      headers: { "Content-Type": "application/json" },
      body
    });

    if (!res.ok) throw new Error(await res.text());
    showMessage("success", `User ${id ? "updated" : "created"} successfully!`);
    clearForm();
    refreshUsers();
  } catch (err) {
    showMessage("error", err.message || "Something went wrong");
  }
});


//Clears the form and any errors when Reset is clicked.
resetBtn.addEventListener("click", clearForm);


//Edit or Delete a User
tableBody.addEventListener("click", async e => {
  //Checks if a button inside the table was clicked.
  const btn = e.target.closest("button");
  if (!btn) return;

  const tr  = btn.closest("tr");
  const id  = tr.dataset.id;

  if (btn.classList.contains("edit")) {
    // Prefills the form with the user's data to update it
    const cells = tr.children;
    userIdField.value = id;
    nameField.value   = cells[1].textContent;
    emailField.value  = cells[2].textContent;
    window.scrollTo({ top: 0, behavior: "smooth" });
  }

// Shows confirmation before sending DELETE request.
// On success, reloads the user list.
  if (btn.classList.contains("delete")) {
    if (!confirm("Delete this user?")) return;
    try {
      const res = await fetch(`${API}/${id}`, { method: "DELETE" });
      if (!res.ok) throw new Error(await res.text());
      showMessage("success", "User deleted");
      refreshUsers();
    } catch (err) {
      showMessage("error", err.message || "Delete failed");
    }
  }
});

//Automatically loads all users when the page finishes loading.
document.addEventListener("DOMContentLoaded", refreshUsers);
