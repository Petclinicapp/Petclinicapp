export const homeInfo = [
  {
    id: 1,
    link: "/our-staff",
    label: "Meet Our Vets",
    info: "Meet the Veterinarians of Vet Clinic. We’re pleased to provide exceptional vet care for your pets.",
    color: "#6AA9C1",
    img: "/images/image3.png",
  },
  {
    id: 2,
    link: "/about",
    label: "Why Choose Us",
    info: "Meet the Veterinarians of Vet Clinic. We’re pleased to provide exceptional vet care for your pets.",
    color: "#018AC1",
    img: "/images/image2.png",
  },
  {
    id: 3,
    link: "/prices",
    label: "Our Prices",
    info: "Meet the Veterinarians of Vet Clinic. We’re pleased to provide exceptional vet care for your pets.",
    color: "#016891",
    img: "/images/image1.png",
  },
];

export const footerServices = [
  { id: 1, title: "Skin and Ear Cytology" },
  { id: 2, title: "Ultrasound Scan Services" },
  { id: 3, title: "Dental Care and Cleanings" },
  { id: 4, title: "Nutritional Counseling" },
  { id: 5, title: "Puppy Wellness Program" },
  { id: 6, title: "Pets Surgical Services" },
];

export const footerLinks = [
  { id: 1, title: "About Us", link: "/" },
  { id: 2, title: "FAQ", link: "/" },
  { id: 3, title: "Our Services", link: "/" },
  { id: 4, title: "Team", link: "/" },
  { id: 5, title: "Prices", link: "/" },
  { id: 6, title: "Blog", link: "/" },
];

export const headerLinks = [
  { id: 1, title: "Home", link: "/" },
  { id: 2, title: "About Us", link: "/about-us" },
  { id: 3, title: "Prices", link: "/prices" },
  { id: 4, title: "Contact", link: "/contacts" },
];

export const pets = [
  {
    petId: "1",
    petName: "Bella",
    species: "Dog",
    breed: "Labrador",
    gender: true, // Male
    age: 3,
    weight: 30.5,
  },
  {
    petId: "2",
    petName: "Luna the Princess",
    species: "Cat",
    breed: "Siamese",
    gender: false, // Female
    age: 2,
    weight: 8.3,
  },
  {
    petId: "3",
    petName: "Max",
    species: "Dog",
    breed: "Bulldog",
    gender: true, // Male
    age: 5,
    weight: 25.0,
  },
  {
    petId: "4",
    petName: "Daisy",
    species: "Rabbit",
    breed: "Himalayan",
    gender: false, // Female
    age: 1,
    weight: 4.2,
  },
];

export const availableAppointments = [
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-13", availableTime: "08:00", booked: false },
      { availableDate: "2025-02-13", availableTime: "10:00", booked: false },
      { availableDate: "2025-02-13", availableTime: "14:00", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-14", availableTime: "09:00", booked: false },
      { availableDate: "2025-02-14", availableTime: "11:00", booked: false },
      { availableDate: "2025-02-14", availableTime: "15:00", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-15", availableTime: "08:30", booked: false },
      { availableDate: "2025-02-15", availableTime: "12:00", booked: false },
      { availableDate: "2025-02-15", availableTime: "16:00", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-20", availableTime: "09:30", booked: false },
      { availableDate: "2025-02-20", availableTime: "13:00", booked: false },
      { availableDate: "2025-02-20", availableTime: "17:00", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-23", availableTime: "08:00", booked: false },
      { availableDate: "2025-02-23", availableTime: "10:30", booked: false },
      { availableDate: "2025-02-23", availableTime: "14:30", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-25", availableTime: "09:00", booked: false },
      { availableDate: "2025-02-25", availableTime: "11:30", booked: false },
      { availableDate: "2025-02-25", availableTime: "15:30", booked: false },
    ],
  },
  {
    doctorId: "1234",
    availableSlots: [
      { availableDate: "2025-02-28", availableTime: "07:30", booked: false },
      { availableDate: "2025-02-28", availableTime: "10:00", booked: false },
      { availableDate: "2025-02-28", availableTime: "13:30", booked: false },
    ],
  },
];
