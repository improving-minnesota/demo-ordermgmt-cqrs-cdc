// Error alert component for displaying error messages
const ErrorAlert = ({ message, onClose }: { message: string; onClose: () => void }) => (
  <div className="fixed top-4 right-4 bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded flex justify-between items-center max-w-md z-50">
    <span>{message}</span>
    <button 
      onClick={onClose} 
      className="ml-4 text-red-700 hover:text-red-900"
    >
      &times;
    </button>
  </div>
);

export default ErrorAlert;
