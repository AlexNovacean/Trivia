function generateActivationCode(email){
  fetch('/activate', {
        method: "PATCH",
        header: {
            "Content-Type": "application/json"
        },
        body: email
    })
      .then(response => generatedCode(response));
}

function generatedCode(response){
    if(response.status === 200){
        const element = document.querySelector(".generate-code");
        const timer = document.querySelector(".generate-code-timer");
        const button = document.querySelector("#generate-button");

        element.classList.remove("hide");
        button.disabled = true;

        const timerMsg = timer.textContent;
        let timeLeft = 120;
        timer.textContent = timerMsg + timeLeft;

        const countdownInterval = setInterval(() => {
            timeLeft--;
            timer.textContent  = timerMsg + timeLeft;

            if (timeLeft <= 0) {
                clearInterval(countdownInterval);
                button.disabled = false;
                element.classList.add("hide");
            }
        }, 1000);
    }
}

function deleteTrivia(button){
    const container = button.closest('.trivia-container');

    fetch('/trivia', {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        },
        body: container.getAttribute('data-id')
    })
        .then(() => container.remove())
        .catch(error => console.error('Failed to delete trivia:', error));
}

function toggleEditMode(button) {
    const container = button.closest('.trivia-container');
    button.classList.add('hidden');
    container.querySelector('.cancel-button').classList.remove('hidden');

    const fields = [
        {selector: '.question', inputType: 'text', id: 'question-input'},
        {selector: '.answers', inputType: 'text', id: 'answers-input'},
        {selector: '.correctAnswers', inputType: 'text', id: 'correct-answer-input'}
    ];

    fields.forEach(field => {
        const span = container.querySelector(field.selector);
        const value = span.textContent;
        span.outerHTML = `<input type="${field.inputType}" id="${field.id}" value="${value}" oninput="checkModification(this)" />`;
    });

    const categorySpan = container.querySelector(".category");
    const categoryValue = categorySpan.textContent;
    categorySpan.outerHTML = createSelect('category', categoryValue, document.querySelector(".add-trivia-button").getAttribute('data-categories'));

    const difficultySpan = container.querySelector(".difficulty");
    const difficultyValue = difficultySpan.textContent;
    difficultySpan.outerHTML = createSelect('difficulty', difficultyValue, document.querySelector(".add-trivia-button").getAttribute('data-difficulties'));
}

function createSelect(name, selectedValue, options) {
    const trimmedOptions = options.slice(1, -1);
    const optionsArray = trimmedOptions.split(',').map(value => value.trim());
    return `<select name="${name}" onchange="checkModification(this)">
            ${optionsArray.map(option => `
                <option value="${option}" ${option === selectedValue ? 'selected' : ''}>${option}</option>
            `).join('')}
            </select>`;
}

function checkModification(element) {
    const container = element.closest('.trivia-container');
    const originalValues = {
        question: container.getAttribute('data-question'),
        answers: container.getAttribute('data-answers'),
        correctAnswer: container.getAttribute('data-correct-answer'),
        category: container.getAttribute('data-category'),
        difficulty: container.getAttribute('data-difficulty')
    };

    const currentValues = {
        question: container.querySelector('#question-input').value,
        answers: container.querySelector('#answers-input').value,
        correctAnswer: container.querySelector('#correct-answer-input').value,
        category: container.querySelector('select[name="category"]').value,
        difficulty: container.querySelector('select[name="difficulty"]').value
    };

    if (JSON.stringify(originalValues) !== JSON.stringify(currentValues)) {
        container.querySelector('.save-button').classList.remove('hidden');
    } else {
        container.querySelector('.save-button').classList.add('hidden');
    }
}

function cancelEdit(button) {
    const container = button.closest('.trivia-container');

    button.classList.add('hidden');
    container.querySelector('.save-button').classList.add('hidden');
    container.querySelector('button:not(.save-button):not(.cancel-button)').classList.remove('hidden');

    container.innerHTML = `
        <div class="trivia-cell"><span class="question">${container.getAttribute('data-question')}</span></div>
        <div class="trivia-cell"><span class="answers">${container.getAttribute('data-answers')}</span></div>
        <div class="trivia-cell"><span class="correctAnswers">${container.getAttribute('data-correct-answer')}</span></div>
        <div class="trivia-cell"><span class="category">${container.getAttribute('data-category')}</span></div>
        <div class="trivia-cell"><span class="difficulty">${container.getAttribute('data-difficulty')}</span></div>
        <div class="trivia-cell">
            <button class="edit-button" onclick="toggleEditMode(this)">Edit</button>
            <button class="delete-button" onclick="deleteTrivia(this)">Delete</button>
            <button class="save-button hidden" onclick="saveTrivia(this)">Save</button>
            <button class="cancel-button hidden" onclick="cancelEdit(this)">Cancel</button>
        </div>
    `;
}

function saveTrivia(button) {
    const container = button.closest('.trivia-container');
    const question = container.querySelector('#question-input').value;
    const answers = container.querySelector('#answers-input').value;
    const correctAnswer = container.querySelector('#correct-answer-input').value;
    const category = container.querySelector('select[name="category"]').value;
    const difficulty = container.querySelector('select[name="difficulty"]').value;

    const id = container.getAttribute('data-id');

    const trivia = {
        id, question, answers, correctAnswer, category, difficulty
    };

    fetch('/trivia', {
        method: 'PATCH',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(trivia)
    })
        .then(() => updateTrivia(container, question, answers, correctAnswer, category, difficulty))
        .catch(error => console.error('Failed to update trivia:', error));
}

function updateTrivia(container, question, answers, correctAnswer, category, difficulty){
    container.setAttribute('data-question', question);
    container.setAttribute('data-answers', answers);
    container.setAttribute('data-correct-answer', correctAnswer);
    container.setAttribute('data-category', category);
    container.setAttribute('data-difficulty', difficulty);

    container.innerHTML = `
            <div class="trivia-cell"><span class="question">${question}</span></div>
            <div class="trivia-cell"><span class="answers">${answers}</span></div>
            <div class="trivia-cell"><span class="correctAnswers">${correctAnswer}</span></div>
            <div class="trivia-cell"><span class="category">${category}</span></div>
            <div class="trivia-cell"><span class="difficulty">${difficulty}</span></div>
            <div class="trivia-cell">
                <button class="edit-button" onclick="toggleEditMode(this)">Edit</button>
                <button class="delete-button" onclick="deleteTrivia(this)">Delete</button>
                <button class="save-button hidden" onclick="saveTrivia(this)">Save</button>
                <button class="cancel-button hidden" onclick="cancelEdit(this)">Cancel</button>
            </div>
        `;
}


function addNewTriviaEntry() {
    const triviaContainer = document.createElement('div');
    triviaContainer.className = 'trivia-container';

    const categories = document.querySelector(".add-trivia-button").getAttribute('data-categories');
    const difficulties = document.querySelector(".add-trivia-button").getAttribute('data-difficulties');

    triviaContainer.innerHTML = `
        <div class="trivia-cell">
        <input type="text" placeholder="Question" />
        <br />
        <span class="valid-question hidden" />
        </div>
        <div class="trivia-cell">
        <input type="text" placeholder="Answers" />
        <br />
        <span class="valid-answers hidden" />
        </div>
        <div class="trivia-cell">
        <input type="text" placeholder="Correct Answer" />
        <br />
        <span class="valid-correct-answer hidden" />
        </div>
        <div class="trivia-cell">
            <select>
                ${extractOptions(categories).map(category => `<option value="${category}">${category}</option>`).join('')}
            </select>
        </div>
        <div class="trivia-cell">
            <select>
                ${extractOptions(difficulties).map(difficulty => `<option value="${difficulty}">${difficulty}</option>`).join('')}
            </select>
        </div>
        <div class="trivia-cell">
            <button onclick="saveNewTrivia(this)">Save</button>
            <button onclick="removeTriviaEntry(this)">Cancel</button>
        </div>
    `;

    let triviaContainers = document.querySelectorAll('.trivia-container');
    triviaContainers[triviaContainers.length - 1].after(triviaContainer);
    triviaContainer.scrollIntoView(true);
}

function extractOptions(options) {
    const trimmedOptions = options.slice(1, -1);
    return trimmedOptions.split(',').map(value => value.trim());
}

function saveNewTrivia(button) {
    const container = button.closest('.trivia-container');
    const inputs = container.querySelectorAll('input');
    const selects = container.querySelectorAll('select');

    const newTrivia = {
        question: inputs[0].value,
        answers: inputs[1].value,
        correctAnswer: inputs[2].value,
        category: selects[0].value,
        difficulty: selects[1].value
    };

    if(validateTrivia(newTrivia, container)) {
        fetch('/trivia', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(newTrivia)
        })
            .then(response => response.json())
            .then(response => {
                container.setAttribute('data-id', response.id);
                container.setAttribute('data-question', response.question);
                container.setAttribute('data-answers', response.answers);
                container.setAttribute('data-correct-answer', response.correctAnswer);
                container.setAttribute('data-category', response.category);
                container.setAttribute('data-difficulty', response.difficulty);

                container.innerHTML = `
            <div class="trivia-cell"><span>${response.question}</span></div>
            <div class="trivia-cell"><span>${response.answers}</span></div>
            <div class="trivia-cell"><span>${response.correctAnswer}</span></div>
            <div class="trivia-cell"><span>${response.category}</span></div>
            <div class="trivia-cell"><span>${response.difficulty}</span></div>
            <div class="trivia-cell">
                <button class="edit-button" onclick="toggleEditMode(this)">Edit</button>
                <button class="delete-button" onclick="deleteTrivia(this)">Delete</button>
                <button class="save-button hidden" onclick="saveTrivia(this)">Save</button>
                <button class="cancel-button hidden" onclick="cancelEdit(this)">Cancel</button>
            </div>
        `;
            })
            .catch(error => {
                console.error('Failed to add new trivia:', error);
                removeTriviaEntry(button);
            });
    }
}

function validateTrivia(trivia, container) {
    const questionValidation = container.querySelector('.valid-question');
    const answersValidation = container.querySelector('.valid-answers');
    const correctAnswersValidation = container.querySelector('.valid-correct-answer');

    let validQuestion = validateQuestion(trivia, container, questionValidation);
    let validAnswers = validateAnswers(trivia, container, answersValidation);
    let validCorrectAnswer = validateCorrectAnswer(trivia, container, correctAnswersValidation);

    return validQuestion && validAnswers && validCorrectAnswer;
}

function validateQuestion(trivia, container, validation) {
    if (!trivia.question) {
        validation.textContent = 'Question cannot be empty.';
        validation.classList.remove('hidden');
        return false;
    }

    if (trivia.question.length < 10) {
        validation.textContent = 'Question must be at least 10 characters long';
        validation.classList.remove('hidden');
        return false;
    }

    if (!trivia.question.endsWith('?')) {
        validation.textContent = 'Question must end with \'?\'';
        validation.classList.remove('hidden');
        return false;
    }

    const firstChar = trivia.question.charAt(0);
    if(firstChar !== firstChar.toUpperCase()) {
        validation.textContent = 'Question must start with uppercase letter';
        validation.classList.remove('hidden');
        return false;
    }

    if (!validation.classList.contains('hidden')) {
        validation.classList.add('hidden');
    }
    return true;
}

function validateAnswers(trivia, container, validation) {
    if(!trivia.answers){
        validation.textContent = 'Answers cannot be empty.';
        validation.classList.remove('hidden');
        return false;
    }

    if (!trivia.answers.includes(",")) {
        validation.textContent = 'Provide answers separated by a comma, only one has to be correct';
        validation.classList.remove('hidden');
        return false;
    }

    let answersCount = trivia.answers.split(',').length;
    if(answersCount !== 4) {
        validation.textContent = 'Provide exactly 4 answers, only one has to be correct';
        validation.classList.remove('hidden');
        return false;
    }

    if (!validation.classList.contains('hidden')) {
        validation.classList.add('hidden');
    }
    return true;
}

function validateCorrectAnswer(trivia, container, validation) {
    if(!trivia.correctAnswer){
        validation.textContent = 'Correct answer cannot be empty.';
        validation.classList.remove('hidden');
        return false;
    }

    if(!trivia.answers.includes(trivia.correctAnswer)){
        validation.textContent = 'Correct answer must be present in the answers section';
        validation.classList.remove('hidden');
        return false;
    }

    if (!validation.classList.contains('hidden')) {
        validation.classList.add('hidden');
    }
    return true;
}

function removeTriviaEntry(button) {
    const container = button.closest('.trivia-container');
    container.remove();
}