import { Link } from "react-router-dom";
import { homeInfo } from "../data";

function Sections() {
  return (
    <div className="flex flex-col lg:flex-row w-full my-10">
      {homeInfo.map((card) => (
        <Link
          key={card.id}
          to={card.link}
          style={{ backgroundColor: card.color }}
          className="flex items-center w-full flex-col p-20 text-center gap-4"
        >
          <img src={card.img} className="w-2/3" />
          <h1 className="text-4xl uppercase text-white">{card.label}</h1>
          <p className="text-white text-xl">{card.info}</p>
        </Link>
      ))}
    </div>
  );
}
export default Sections;
