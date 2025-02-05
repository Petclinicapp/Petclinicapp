function Input({
  onChange,
  value,
  type,
  name,
  placeholder = null,
  label = null,
}) {
  return (
    <div className="flex flex-col">
      <span className="mb-1 text-gray-500">{label}</span>
      <input
        id={name}
        placeholder={placeholder}
        type={type}
        name={name}
        onChange={onChange}
        value={value}
        className="bg-[#eeeee9] rounded w-full p-4 border border-transparent p-2 focus:outline-none focus:ring-1 focus:ring-gray-400"
      />
    </div>
  );
}
export default Input;
