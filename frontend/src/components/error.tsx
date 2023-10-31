function Error({text}: {text: string}) {
  return (
    <div className="d-flex justify-content-center mt-3">
      <div className="alert alert-danger" role="alert">
        {text}
      </div>
    </div>
  )
}

export default Error;
