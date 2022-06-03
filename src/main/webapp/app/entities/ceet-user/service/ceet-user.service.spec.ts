import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICeetUser, CeetUser } from '../ceet-user.model';

import { CeetUserService } from './ceet-user.service';

describe('CeetUser Service', () => {
  let service: CeetUserService;
  let httpMock: HttpTestingController;
  let elemDefault: ICeetUser;
  let expectedResult: ICeetUser | ICeetUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(CeetUserService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      phone: 'AAAAAAA',
      address: 'AAAAAAA',
      documentNumber: 'AAAAAAA',
      documentType: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a CeetUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new CeetUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a CeetUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          phone: 'BBBBBB',
          address: 'BBBBBB',
          documentNumber: 'BBBBBB',
          documentType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a CeetUser', () => {
      const patchObject = Object.assign(
        {
          phone: 'BBBBBB',
          address: 'BBBBBB',
          documentNumber: 'BBBBBB',
          documentType: 'BBBBBB',
        },
        new CeetUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of CeetUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          phone: 'BBBBBB',
          address: 'BBBBBB',
          documentNumber: 'BBBBBB',
          documentType: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a CeetUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addCeetUserToCollectionIfMissing', () => {
      it('should add a CeetUser to an empty array', () => {
        const ceetUser: ICeetUser = { id: 123 };
        expectedResult = service.addCeetUserToCollectionIfMissing([], ceetUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ceetUser);
      });

      it('should not add a CeetUser to an array that contains it', () => {
        const ceetUser: ICeetUser = { id: 123 };
        const ceetUserCollection: ICeetUser[] = [
          {
            ...ceetUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addCeetUserToCollectionIfMissing(ceetUserCollection, ceetUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a CeetUser to an array that doesn't contain it", () => {
        const ceetUser: ICeetUser = { id: 123 };
        const ceetUserCollection: ICeetUser[] = [{ id: 456 }];
        expectedResult = service.addCeetUserToCollectionIfMissing(ceetUserCollection, ceetUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ceetUser);
      });

      it('should add only unique CeetUser to an array', () => {
        const ceetUserArray: ICeetUser[] = [{ id: 123 }, { id: 456 }, { id: 21009 }];
        const ceetUserCollection: ICeetUser[] = [{ id: 123 }];
        expectedResult = service.addCeetUserToCollectionIfMissing(ceetUserCollection, ...ceetUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ceetUser: ICeetUser = { id: 123 };
        const ceetUser2: ICeetUser = { id: 456 };
        expectedResult = service.addCeetUserToCollectionIfMissing([], ceetUser, ceetUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ceetUser);
        expect(expectedResult).toContain(ceetUser2);
      });

      it('should accept null and undefined values', () => {
        const ceetUser: ICeetUser = { id: 123 };
        expectedResult = service.addCeetUserToCollectionIfMissing([], null, ceetUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ceetUser);
      });

      it('should return initial array if no CeetUser is added', () => {
        const ceetUserCollection: ICeetUser[] = [{ id: 123 }];
        expectedResult = service.addCeetUserToCollectionIfMissing(ceetUserCollection, undefined, null);
        expect(expectedResult).toEqual(ceetUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
