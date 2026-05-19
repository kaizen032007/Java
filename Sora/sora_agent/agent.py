import os
from flask import Flask, request, jsonify
from flask_cors import CORS
import google.generativeai as genai
from dotenv import load_dotenv

load_dotenv()

app = Flask(__name__)
app.json.ensure_ascii = False
CORS(app)

# Configure Gemini
api_key = os.getenv("GEMINI_API_KEY")
genai.configure(api_key=api_key)
model = genai.GenerativeModel('gemini-flash-latest')

@app.route('/advise', methods=['POST'])
def advise():
    data = request.json
    
    # Financial context from Java app
    balance = data.get('balance', 0)
    income = data.get('income', 0)
    spent = data.get('spent', 0)
    goals = data.get('goals', [])
    subscriptions = data.get('subscriptions', [])
    user_name = data.get('user_name', 'User')
    user_prompt = data.get('user_prompt', 'Give me general financial advice.')

    # Construct the prompt
    prompt = f"""
    You are the 'Sora Agent', a friendly and protective AI financial assistant for {user_name}.
    Current Financial State:
    - Total Balance: ₱{balance:,.2f}
    - Total Income: ₱{income:,.2f}
    - Total Spent: ₱{spent:,.2f}

    The user is asking: "{user_prompt}"

    TASK:
    Respond to the user's question directly while keeping their financial state in mind.
    If they are asking to spend money and their balance is low, be protective and discourage it.
    Provide a short (2-4 sentences), punchy, and helpful response. 
    Speak like a modern, smart assistant. Use a bit of personality.
    Don't use markdown formatting, just plain text.
    """

    try:
        response = model.generate_content(prompt)
        # Check if the response was blocked by safety filters
        if response.candidates and response.candidates[0].finish_reason == 3: # SAFETY
             return jsonify({"advice": "I'm worried about your finances, but I can't quite put it into words safely. Keep an eye on that balance!"})
        
        advice = response.text.strip() if response.text else "Keep tracking your expenses to stay ahead!"
        return jsonify({"advice": advice})
    except Exception as e:
        import sys
        print(f"Error in Sora Agent: {str(e)}")
        sys.stdout.flush()
        # Check if it's an API key issue
        if "API_KEY_INVALID" in str(e):
             return jsonify({"advice": "Your Gemini API Key seems to be invalid. Please check it in sora_agent/.env"}), 401
        return jsonify({"advice": f"I'm having trouble: {str(e)}"}), 500

if __name__ == '__main__':
    print("Sora Agent is waking up on http://127.0.0.1:5000")
    app.run(port=5000)
