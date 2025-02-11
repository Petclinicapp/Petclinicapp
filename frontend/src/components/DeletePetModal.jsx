function DeletePetModal({ isOpen, onClose, onConfirm, petName }) {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 bg-black/50 bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white p-6 rounded-lg w-[500px]">
        <h2 className="text-xl font-semibold">Confirm Deletion</h2>
        <p>
          Are you sure you want to delete the pet "
          <span className="capitalize font-bold">{petName}</span>"?
        </p>
        <div className="flex justify-end gap-4 mt-4">
          <button
            onClick={onClose}
            className="bg-gray-300 px-4 py-2 rounded cursor-pointer hover:bg-gray-400 transition-all duration-300 ease-in-out"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="bg-red-700 text-white px-4 py-2 rounded cursor-pointer hover:bg-red-800 transition-all duration-300 ease-in-out"
          >
            Confirm
          </button>
        </div>
      </div>
    </div>
  );
}
export default DeletePetModal;
