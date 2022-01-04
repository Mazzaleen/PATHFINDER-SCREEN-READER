function TextBox(props) {

    const handleChange = (e) => {
        props.change(e.target.value)
    }

    return (
        <div className="TextBox">
            <header className="TextBox-header">
                <label onChange={handleChange}>
                    {props.label}
                    <input type={props.type} >{props.input}</input>
                </label><br />
            </header>


        </div>

    );
}

export default TextBox;