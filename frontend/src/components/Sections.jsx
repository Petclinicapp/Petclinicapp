import { Link } from "react-router-dom";
import { homeInfo } from "../data";

function Sections() {
  return (
    <div className="flex w-full mt-10">
      {homeInfo.map((card) => (
        <Link
          key={card.id}
          to={card.link}
          style={{ backgroundColor: card.color }}
          className="flex w-full flex-col p-20 text-center gap-4"
        >
          <img src={card.img} />
          <h1 className="text-4xl uppercase text-white">{card.label}</h1>
          <p className="text-white text-xl">{card.info}</p>
        </Link>
      ))}
    </div>
  );
}
export default Sections;
